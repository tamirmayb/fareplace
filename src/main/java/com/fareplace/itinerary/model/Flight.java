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
@Document(collection = "flights")
public class Flight {

    @Id
    private @NonNull
    String internalId;

    @JsonProperty
    private String flightNumber;

    @JsonProperty
    private String from;

    @JsonProperty
    private String to;

    @JsonProperty
    private String date;

    @JsonProperty
    private String departureTime;

    @JsonProperty
    private int duration;

    public static Flight of(String date, String flightNumber, String from, String to, String departureTime, int duration) {
        return Flight.builder()
                .internalId(flightNumber
                        .concat("-")
                        .concat(date))
                .flightNumber(flightNumber)
                .from(from)
                .to(to)
                .date(date)
                .departureTime(departureTime)
                .duration(duration).build();
    }
}
