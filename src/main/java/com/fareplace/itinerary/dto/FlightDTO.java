package com.fareplace.itinerary.dto;

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

    public static FlightDTO of(String flightNumber, String departureDate, String from, String to, String departureTime, int duration) {
        return FlightDTO.builder()
                .flightNumber(flightNumber)
                .date(departureDate)
                .from(from)
                .to(to)
                .departureTime(departureTime)
                .duration(duration)
                .build();
    }
}
