package com.fareplace.itinerary.services;

import com.fareplace.itinerary.config.MongoConfig;
import com.fareplace.itinerary.model.Flight;
import com.fareplace.itinerary.utils.FilesHandler;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class FlightsService {
    private static final String COMMA_DELIMITER = ",";
    private static final Logger log = LogManager.getLogger(FlightsService.class);

    private static final String PATH_TO_CSV = "flights.csv";

    private final MongoConfig mongo;


    public void loadFlights() {
        List<Flight> flights = new ArrayList<>();
        MongoOperations mongoOps = new MongoTemplate(mongo.mongoClient(), mongo.getDatabaseName());

        try (BufferedReader br = new BufferedReader(new FileReader(FilesHandler.readFile(PATH_TO_CSV)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                try {
                    flights.add(createFlight(values));
                } catch (Exception e) {
                    log.warn("Could not create a flight from input " + line);
                }
            }

            this.saveFlightsAsMongoBatch(mongoOps, flights);
        } catch (FileNotFoundException e) {
            log.error("Flights input file not found " + PATH_TO_CSV);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Flight createFlight(String[] values) {
        return Flight.of(values[0],
                values[1],
                values[2],
                values[3],
                values[4],
                Integer.parseInt(values[5])
        );
    }

    private void saveFlightsAsMongoBatch(MongoOperations mongoOps, List<Flight> requestListBatch) {
        try {
        BulkOperations bulkOps = mongoOps.bulkOps(BulkOperations.BulkMode.UNORDERED, Flight.class);
        bulkOps.insert(requestListBatch);
        bulkOps.execute();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
