package com.fareplace.itinerary.dto;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class ItineraryResult {
    private List<FlightDTO> Flights;
    private Double totalPrice;
}
