package com.fareplace.itinerary.repositories;

import com.fareplace.itinerary.model.Flight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightsRepository extends MongoRepository<Flight, String> {

    Optional<List<Flight>> findByDate(String date);

}

