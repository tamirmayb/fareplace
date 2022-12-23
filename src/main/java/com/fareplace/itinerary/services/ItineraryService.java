package com.fareplace.itinerary.services;

import com.fareplace.itinerary.dto.FlightDTO;
import com.fareplace.itinerary.dto.ItineraryResult;
import com.fareplace.itinerary.model.Flight;
import com.fareplace.itinerary.model.FlightPrice;
import com.fareplace.itinerary.repositories.FlightsPricesRepository;
import com.fareplace.itinerary.repositories.FlightsRepository;
import com.fareplace.itinerary.utils.FormattedDateMatcher;
import com.fareplace.itinerary.utils.ItineraryCalculator;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@AllArgsConstructor
@Service
public class ItineraryService {

    private final Environment env;

    private final static String EXTRA_CONN_TIME_PROP_NAME = "itinerary.extra.conn.time.mins";

    private static final Logger log = LogManager.getLogger(ItineraryService.class);

    private static final long MINUTE = 60;

    private final FlightsRepository flightsRepository;
    private final FlightsPricesRepository flightsPricesRepository;

    public List<ItineraryResult> getPriceWithConnections(String from, String to, String date, Integer connectionsCount) {
        if(!FormattedDateMatcher.matches(date)) {
            throw new IllegalArgumentException("Illegal date cannot search flights " + date);
        }

        List<ItineraryResult> results = new ArrayList<>();

        log.info("starting getPriceWithConnections from = " + from + " to = " + to + " date = " + date);

        Optional<List<Flight>> byDate = flightsRepository.findByDate(date);
        if(byDate.isPresent()) {
            List<Flight> flightsByDate = byDate.get();
            Map<String, Flight> map = new HashMap<>();
            flightsByDate.forEach(f->{
                map.put(f.getInternalId(), f);
            });

            Map<String, Optional<FlightPrice>> flightsPricesCache = new HashMap<>();

            List<List<String>> calculatedItineraries = ItineraryCalculator.calculateItineraries(flightsByDate, from, to);
            calculatedItineraries.forEach(calcItinerary -> {
                calcItinerary.forEach(itineraryFlightIds -> {
                    List<FlightDTO> flightsDtos = new ArrayList<>();
                    List<String> suggestItineraries = Arrays.asList(itineraryFlightIds.split(","));
                    if(suggestItineraries.size() <= connectionsCount) {
                        Collections.singletonList(suggestItineraries).forEach(itinerary -> {
                            itinerary.forEach(flightId -> {

                                // since flights cna exist in multiple itineraries we use a local cache map so that
                                // we'll only fetch from db when cache is missing the flight price being searched.
                                if(!flightsPricesCache.containsKey(flightId)) {
                                    Optional<FlightPrice> byInternalId = flightsPricesRepository.findByInternalId(flightId);
                                    flightsPricesCache.put(flightId, byInternalId);
                                }
                                if (flightsPricesCache.get(flightId).isPresent()) {
                                    FlightPrice flightPrice = flightsPricesCache.get(flightId).get();
                                    // just to make sure
                                    if (flightPrice.getPrice() > 0 && flightPrice.getAvailableSeats() > 0) {
                                        flightsDtos.add(FlightDTO.fromFlight(map.get(flightPrice.getInternalId()), flightPrice));
                                    }
                                }
                            });
                            // check all flight times to make sure that there are no broken itineraries, sum the prices for totalPrice and return the results
                            if (flightsDtos.size() > 0 && checkFlightTimes(flightsDtos)) {
                                double totalPrice = flightsDtos.stream().mapToDouble(FlightDTO::getPrice).sum();
                                results.add(new ItineraryResult(flightsDtos, totalPrice));
                            }
                        });
                    }
                });
            });
        }
        log.info("getPriceWithConnections done. Found = " + results.size() + " possible itineraries");
        return results;
    }

    private boolean checkFlightTimes(List<FlightDTO> flightDTOs) {
        String extraConnTime = env.getProperty(EXTRA_CONN_TIME_PROP_NAME);

        if(flightDTOs.size() == 1) {
            return true;
        }
        try {
            FlightDTO lastFlight = flightDTOs.get(flightDTOs.size() - 1);
            OffsetDateTime lastFlightDepTime = getFlightOffsetDateTime(lastFlight);

            for (int i = flightDTOs.size() - 2; i >= 0; i--) {
                FlightDTO dto = flightDTOs.get(i);
                OffsetDateTime currFlightDepTime = getFlightOffsetDateTime(dto);
                int currFlightDuration = dto.getDuration();
                assert extraConnTime != null;
                if(currFlightDepTime.toInstant()
                        .isAfter(lastFlightDepTime.toInstant()
                                // adding 30 mins extra after landing to find the next flight
                                .minusSeconds((currFlightDuration + Integer.parseInt(extraConnTime)) * MINUTE))) {
                    return false;
                } else {
                    lastFlightDepTime = currFlightDepTime;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private OffsetDateTime getFlightOffsetDateTime(FlightDTO dto) {
        String flightDateTimeStr = dto.getDate() + "T" + dto.getDepartureTime();
        return OffsetDateTime.parse(flightDateTimeStr);
    }
}
