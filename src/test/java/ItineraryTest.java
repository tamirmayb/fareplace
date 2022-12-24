import com.fareplace.itinerary.dto.FlightDTO;
import com.fareplace.itinerary.dto.ItineraryResult;
import com.fareplace.itinerary.model.Flight;
import com.fareplace.itinerary.model.FlightPrice;
import com.fareplace.itinerary.repositories.FlightsPricesRepository;
import com.fareplace.itinerary.repositories.FlightsRepository;
import com.fareplace.itinerary.services.IItineraryService;
import com.fareplace.itinerary.services.ItineraryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class ItineraryTest {
    IItineraryService itineraryService;

    private FlightsRepository flightsRepository;
    private FlightsPricesRepository flightsPricesRepository;

    private final List<ItineraryResult> itineraryResults = new ArrayList<>();

    List<Flight> flightsResults = new ArrayList<>();

    FlightPrice flightPrice;

    @Before
    public void init() {
        Flight flight = Flight.of(
                "2022-01-13",
                "001",
                "TLV",
                "JFK",
                "00:00",
                300);

        flightPrice = FlightPrice.of(
                "2022-01-13",
                "001",
                100,
                750F);

        flightsResults.add(flight);

        itineraryResults.add(new ItineraryResult(Collections
                .singletonList(FlightDTO
                        .fromFlight(flight, flightPrice)),
                750D));



        Environment env = mock(Environment.class);
        flightsRepository = mock(FlightsRepository.class);
        flightsPricesRepository = mock(FlightsPricesRepository.class);
        itineraryService = new ItineraryService(env, flightsRepository, flightsPricesRepository);
    }

    @Test()
    @Description("Single Flight happy path")
    public void singleFlight() {
        when(this.flightsRepository.findByDate("2022-01-13"))
                .thenAnswer(ans-> Optional.of(flightsResults));

        when(this.flightsPricesRepository.findByInternalId("001-2022-01-13"))
                .thenAnswer(ans-> Optional.of(flightPrice));

        List<ItineraryResult> priceWithConnections = itineraryService.getPriceWithConnections("tlv", "jfk", "2022-01-13", 4);
        System.out.println(priceWithConnections);

        assertEquals(1, priceWithConnections.size());
        assertEquals(itineraryResults, priceWithConnections);
    }

    @Test()
    @Description("Flight No Seats")
    public void singleFlightNoSeats() {
        when(this.flightsRepository.findByDate("2022-01-13"))
                .thenAnswer(ans-> Optional.of(flightsResults));

        when(this.flightsPricesRepository.findByInternalId("001-2022-01-13"))
                .thenAnswer(ans-> Optional.of(FlightPrice.of(
                        "2022-01-13",
                        "001",
                        0,
                        750F)));

        List<ItineraryResult> priceWithConnections = itineraryService.getPriceWithConnections("tlv", "jfk", "2022-01-13", 4);
        System.out.println(priceWithConnections);

        assertEquals(0, priceWithConnections.size());
        assertEquals(new ArrayList<>(), priceWithConnections);
    }

    @Test()
    @Description("Flight not found")
    public void singleFlightNotFound() {
        when(this.flightsRepository.findByDate("2022-01-13"))
                .thenAnswer(ans-> Optional.of(flightsResults));

        when(this.flightsPricesRepository.findByInternalId("001-2022-01-13"))
                .thenAnswer(ans-> Optional.of(flightPrice));

        List<ItineraryResult> priceWithConnections = itineraryService.getPriceWithConnections("tlv", "mex", "2022-01-13", 4);
        System.out.println(priceWithConnections);

        assertEquals(0, priceWithConnections.size());
        assertEquals(new ArrayList<>(), priceWithConnections);
    }
}
