package com.fareplace.itinerary;

import com.fareplace.itinerary.dto.ItineraryResult;
import com.fareplace.itinerary.services.ItineraryService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value="itinerary")
public class ItineraryController {

    private final ItineraryService itineraryService;

    @GetMapping("/priceWithConnections")
    @ApiOperation(value = "",  notes = "Searches inventory for an itinerary between two airports, which may include a connecting flight")
    public ResponseEntity<List<ItineraryResult>> getPriceWithConnections(@RequestParam String from, @RequestParam String to, @RequestParam String date) {
        return ResponseEntity.ok(itineraryService.getPriceWithConnections(from, to, date));
    }
}
