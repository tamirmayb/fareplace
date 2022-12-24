package com.fareplace.itinerary.utils;

import com.fareplace.itinerary.model.Flight;

import java.util.*;

public class ItineraryCalculator {

    public static List<List<String>> calculateItineraries(List<Flight> flights, String departure, String destination) {
        List<List<String>> itineraries = new ArrayList<>();
        calculateItineraryRecursive(flights, departure, destination, new Hashtable<>(), "", itineraries);
        return itineraries;
    }

    private static void calculateItineraryRecursive(List<Flight> flights, String departure, String destination,
                                                    Hashtable<String, Boolean> visited, String itineraryFlightsIds,
                                                    List<List<String>> itineraries) {
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
                    itineraries.add(Arrays.asList(tmpIds.split(", ")));
                }
                // once from airport is filled we can check matching destinations for connections
                calculateItineraryRecursive(flights, flight.getTo(), destination, newVisited, tmpIds, itineraries);
            }
        }
        if (toTrue) {
            if (! visited.containsKey(departure)) visited.put(departure, true);
        }
    } // end of calculateItineraries

}
