package com.fareplace.itinerary.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "prices")
public class Price {

    @Id
    private @NonNull
    String id = UUID.randomUUID().toString();

    @JsonProperty
    private String internalId;

    @JsonProperty
    private String flightNumber;

    @JsonProperty
    private Date departureDate;

    @JsonProperty
    private Float price;


    public static Price of(String flightNumber, Date departureDate, Float price) {
        return Price.builder()
                .id(UUID.randomUUID().toString())
                .internalId(flightNumber
                        .concat("-")
                        .concat(String.valueOf(departureDate.getTime())))
                .flightNumber(flightNumber)
                .departureDate (departureDate)
                .price(price)
                .build();
    }
}
