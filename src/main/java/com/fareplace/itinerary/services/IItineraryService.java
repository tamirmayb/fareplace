package com.fareplace.itinerary.services;

import com.fareplace.itinerary.dto.ItineraryResult;

import java.util.List;

public interface IItineraryService {
    List<ItineraryResult> getPriceWithConnections(String from, String to, String date, Integer maxConnections);
}