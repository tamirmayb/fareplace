package com.fareplace.itinerary.services;

import com.fareplace.itinerary.dto.ItineraryResult;
import com.fareplace.itinerary.model.Flight;
import com.fareplace.itinerary.repositories.ItineraryRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ItineraryService {
    private static final Logger log = LogManager.getLogger(ItineraryService.class);

    private final ItineraryRepository itineraryRepository;

    public List<ItineraryResult> getPriceWithConnections(String from, String to, String date) {
        Optional<List<Flight>> byFromAndDate = itineraryRepository.findByFromAndDate(from, date);
        Optional<List<Flight>> byToAndDate = itineraryRepository.findByToAndDate(to, date);
        return null;
    }
}
