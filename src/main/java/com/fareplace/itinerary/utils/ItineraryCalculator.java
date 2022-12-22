package com.fareplace.itinerary.utils;

import com.fareplace.itinerary.model.Flight;

import java.util.*;

public class ItineraryCalculator {
    private static final List<List<String>> FLIGHT_LIST = new ArrayList<>();

    public static List<List<String>> calculateFlights(List<Flight> flights, String departure, String destination) {
        Hashtable<String, Boolean> visited = new Hashtable<>();
        calculateFlightsRecursive(flights, departure, destination, visited, "");
        return FLIGHT_LIST;
    }

    private static void calculateFlightsRecursive(List<Flight> flights, String departure, String destination, Hashtable<String, Boolean> visited, String path) {
        if (Objects.equals(departure, destination) || (visited.containsKey(departure) && visited.get(departure)))
            return;

        boolean toTrue = false;
        for (Flight f: flights) {
            if (f.getFrom().equals(departure)) {
                Hashtable<String, Boolean> newVisited = new Hashtable<>(visited);
                if (!newVisited.containsKey(departure)) {
                    newVisited.put(departure, true);
                }
                toTrue = true;

                String tmpPath = (path.isEmpty() ? "" : path + ",") + f.getInternalId();
                if (f.getTo().equals(destination)) {
                    FLIGHT_LIST.add(Arrays.asList(tmpPath.split(", ")));
                }
                calculateFlightsRecursive(flights, f.getTo(), destination, newVisited, tmpPath);
            }
        }
        if (toTrue) {
            if (! visited.containsKey(departure)) visited.put(departure, true);
        }
    } // end of calculateFlight

}
