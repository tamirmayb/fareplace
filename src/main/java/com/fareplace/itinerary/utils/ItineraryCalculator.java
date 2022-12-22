package com.fareplace.itinerary.utils;

import com.fareplace.itinerary.model.Flight;

import java.util.*;

public class ItineraryCalculator {
    private static final List<List<String>> FLIGHT_LIST = new ArrayList<>();

    public static List<List<String>> calculateItineraries(List<Flight> flights, String departure, String destination) {
        FLIGHT_LIST.clear();
        Hashtable<String, Boolean> visited = new Hashtable<>();
        calculateItineraryRecursive(flights, departure, destination, visited, "");
        return FLIGHT_LIST;
    }

    private static void calculateItineraryRecursive(List<Flight> flights, String departure, String destination, Hashtable<String, Boolean> visited, String itineraryFlightsIds) {
        if (Objects.equals(departure, destination) || (visited.containsKey(departure) && visited.get(departure)))
            return;

        // iterate recursively over flights until a list of matching flights is formed
        boolean toTrue = false;
        for (Flight flight: flights) {
            if (flight.getFrom().equals(departure)) {
                Hashtable<String, Boolean> newVisited = new Hashtable<>(visited);
                if (!newVisited.containsKey(departure)) {
                    newVisited.put(departure, true);
                }
                toTrue = true;

                String tmpIds = (itineraryFlightsIds.isEmpty() ? "" : itineraryFlightsIds + ",") + flight.getInternalId();
                if (flight.getTo().equals(destination)) {
                    FLIGHT_LIST.add(Arrays.asList(tmpIds.split(", ")));
                }
                // once from airport is filled we can check matching destinations for connections
                calculateItineraryRecursive(flights, flight.getTo(), destination, newVisited, tmpIds);
            }
        }
        if (toTrue) {
            if (! visited.containsKey(departure)) visited.put(departure, true);
        }
    } // end of calculateItineraries

}
