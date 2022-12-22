package com.fareplace.itinerary.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "prices")
public class FlightPrice {

    @JsonProperty
    private @NonNull
    String internalId;

    @JsonProperty
    private String flightNumber;

    @JsonProperty
    private String departureDate;

    @JsonProperty
    private Float price;

    @JsonProperty
    private Integer availableSeats;


    public static FlightPrice of(String departureDate, String flightNumber, Integer availableSeats, Float price) {
        return FlightPrice.builder()
                .internalId(flightNumber
                        .concat("-")
                        .concat(departureDate))
                .departureDate (departureDate)
                .flightNumber(flightNumber)
                .availableSeats(availableSeats)
                .price(price)
                .build();
    }
}
