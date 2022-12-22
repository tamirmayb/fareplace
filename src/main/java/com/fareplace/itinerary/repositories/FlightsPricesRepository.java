package com.fareplace.itinerary.repositories;

import com.fareplace.itinerary.model.FlightPrice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightsPricesRepository extends MongoRepository<FlightPrice, String> {
    @Query(value="{ 'internalId' : ?0, 'availableSeats' : { $gt: 0 }, 'price' : { $gt: 0 } }")
    Optional<FlightPrice> findByInternalId(String internalId);
}

