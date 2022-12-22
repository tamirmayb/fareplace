package com.fareplace.itinerary;

import com.fareplace.itinerary.services.ItineraryService;
import com.force.api.ApiError;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value="itinerary")
public class ItineraryController {

    private final ItineraryService itineraryService;

    @GetMapping("/priceWithConnections")
    @ApiOperation(value = "",  notes = "Searches inventory for an itinerary between two airports, which may include a connecting flight")
    public ResponseEntity<Object> getPriceWithConnections(@RequestParam(name = "Departure") String departure,
                                                          @RequestParam(name = "Destination") String destination,
                                                          @RequestParam(name = "Flight Date") String flightDate,
                                                          @RequestParam(required = false, defaultValue = "2", name = "Max Connections") int maxConnections) {
        try {
            return ResponseEntity.ok(itineraryService.getPriceWithConnections(departure.toUpperCase(), destination.toUpperCase(), flightDate, maxConnections));
        } catch (Exception e) {
            ApiError apiError = new ApiError();
            apiError.setMessage(e.getLocalizedMessage());
            apiError.setApiErrorCode("400");
            return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }
}
