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

    @Id
    private @NonNull
    String internalId;

    @JsonProperty
    private String flightNumber;

    @JsonProperty
    private String departureDate;

    @JsonProperty
    private Float price;

    @JsonProperty
    private int availableSeats;


    public static FlightPrice of(String flightNumber, String departureDate, int availableSeats, Float price) {
        return FlightPrice.builder()
                .internalId(flightNumber
                        .concat("-")
                        .concat(departureDate))
                .flightNumber(flightNumber)
                .departureDate (departureDate)
                .price(price)
                .availableSeats(availableSeats)
                .build();
    }
}
