package com.fareplace.itinerary.services;

import com.fareplace.itinerary.config.MongoConfig;
import com.fareplace.itinerary.model.FlightPrice;
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
public class FlightsPricesService {
    private static final String COMMA_DELIMITER = ",";
    private static final String PATH_TO_CSV = "prices.csv";
    private static final Logger log = LogManager.getLogger(FlightsPricesService.class);
    private final MongoConfig mongo;


    public void loadFlightsPrices() {
        List<FlightPrice> flightPrices = new ArrayList<>();
        MongoOperations mongoOps = new MongoTemplate(mongo.mongoClient(), mongo.getDatabaseName());

        try (BufferedReader br = new BufferedReader(new FileReader(FilesHandler.readFile(PATH_TO_CSV)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                try {
                    flightPrices.add(createFlightPrice(values));
                } catch (Exception e) {
                    log.warn("Could not create a flight price from input " + line);
                }
            }
            this.savePricesAsMongoBatch(mongoOps, flightPrices);
        } catch (FileNotFoundException e) {
            log.error("Flights prices input file not found " + PATH_TO_CSV);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FlightPrice createFlightPrice(String[] values) {
        return FlightPrice.of(values[0],
                values[1],
                Integer.parseInt(values[2]),
                Float.parseFloat(values[3])
        );
    }

    private void savePricesAsMongoBatch(MongoOperations mongoOps, List<FlightPrice> requestListBatch) {
        try {
            BulkOperations bulkOps = mongoOps.bulkOps(BulkOperations.BulkMode.UNORDERED, FlightPrice.class);
            bulkOps.insert(requestListBatch);
            bulkOps.execute();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
