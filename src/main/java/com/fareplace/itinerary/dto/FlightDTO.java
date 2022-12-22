package com.fareplace.itinerary.dto;

import com.fareplace.itinerary.model.Flight;
import com.fareplace.itinerary.model.FlightPrice;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.OffsetTime;

@Getter
@Setter
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {

    @JsonProperty
    private String flightNumber;

    @JsonProperty
    private String date;

    @JsonProperty
    private String from;

    @JsonProperty
    private String to;

    @JsonProperty
//    private OffsetTime departureTime;
    private String departureTime;

    @JsonProperty
    private float price;

    @JsonProperty
    private int availableSeats;

    @JsonProperty
    private int duration;

    public static FlightDTO fromFlight(Flight flight, FlightPrice price) {
        return FlightDTO.builder()
                .flightNumber(flight.getFlightNumber())
                .from(flight.getFrom())
                .to(flight.getTo())
                .date(flight.getDate())
                .departureTime(flight.getDepartureTime())
                .duration(flight.getDuration())
                .availableSeats(price.getAvailableSeats())
                .price(price.getPrice())
                .build();
    }
}
