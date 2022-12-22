package com.fareplace.itinerary.services;

import com.fareplace.itinerary.dto.FlightDTO;
import com.fareplace.itinerary.dto.ItineraryResult;
import com.fareplace.itinerary.model.Flight;
import com.fareplace.itinerary.model.FlightPrice;
import com.fareplace.itinerary.repositories.FlightsPricesRepository;
import com.fareplace.itinerary.repositories.FlightsRepository;
import com.fareplace.itinerary.utils.ItineraryCalculator;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class ItineraryService {
    private static final Logger log = LogManager.getLogger(ItineraryService.class);

    private final FlightsRepository flightsRepository;
    private final FlightsPricesRepository flightsPricesRepository;

    public List<ItineraryResult> getPriceWithConnections(String from, String to, String date, Integer connectionsCount) {
        List<ItineraryResult> results = new ArrayList<>();

        Optional<List<Flight>> byDate = flightsRepository.findByDate(date);
        if(byDate.isPresent()) {
            List<Flight> flightsByDate = byDate.get();
            Map<String, Flight> map = new HashMap<>();
            flightsByDate.forEach(f->{
                map.put(f.getInternalId(), f);
            });

            List<List<String>> calculatedFlightsIds = ItineraryCalculator.calculateFlights(flightsByDate, from, to);
            calculatedFlightsIds.forEach(ids -> {
                    ids.forEach(itineraryFlightIds -> {
                        List<FlightDTO> flightsDtos = new ArrayList<>();
                        List<String> suggestItineraries = Arrays.asList(itineraryFlightIds.split(","));
                        if(suggestItineraries.size() <= connectionsCount) {
                            Collections.singletonList(suggestItineraries).forEach(itinerary -> {
                                itinerary.forEach(flightId -> {
                                    Optional<FlightPrice> byInternalId = flightsPricesRepository.findByInternalId(flightId);
                                    if (byInternalId.isPresent()) {
                                        FlightPrice flightPrice = byInternalId.get();
                                        if (flightPrice.getPrice() > 0 && flightPrice.getAvailableSeats() > 0) {
                                            flightsDtos.add(FlightDTO.fromFlight(map.get(flightPrice.getInternalId()), flightPrice));
                                        }
                                        if (flightsDtos.size() > 0) {
                                            double totalPrice = flightsDtos.stream().mapToDouble(FlightDTO::getPrice).sum();
                                            results.add(new ItineraryResult(flightsDtos, totalPrice));
                                        }
                                    }
                                });
                            });
                        }
                    });
            });
        }
        return results;
    }
}
