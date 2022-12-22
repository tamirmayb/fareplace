# FarePlace - Backend Developer Assignment
### Author: Tamir Mayblat

This is the Itinerary calculator application for the BE-Challenge, developed using Java as a spring boot application.

### Prerequisite
* Java and tomcat
* Run ```mvn clean install```
* Make sure that both csv files are located in the resources folder.  
* Connection to Mongodb is also provided by default and can be changed if needed by modifying the fields starting with ```db.``` in application.properties.
* Note that I've already run a process which fills the db from the csv files, the code for this process can be found in FlightsService and FlightsPricesService.

### Using the web service
* Start the server and go to http://127.0.0.1:8080/fareplace/api/swagger-ui.html#/itinerary-controller/getPriceWithConnections
* You should have access to Swagger api page which controls the web service.

### Features
You can find the following api in the web service:

###### GET /getPriceWithConnections -
* This searches flights by date and finds matching itineraries.
* Once found the flight/s for each itinerary are matched to the prices table to make sure the flight has available seats and can be sold, if not the flight will not be shown in the results section.
* The flights are also checked for their departure times to make sure that no flight would be missed, to be on the safe side 30 mins are added to this calculation.
* Note that you can also change the number of possible connections using the maxConnections field.
* If found results should be displayed like this:
```
[
  {
    "totalPrice": 552,
    "flights": [
      {
        "flightNumber": "1010",
        "date": "2022-01-13",
        "from": "TLV",
        "to": "MAD",
        "departureTime": "08:31+02:00",
        "price": 552,
        "availableSeats": 10,
        "duration": 330
      }
    ]
  },
]
```

Please let me know if anything is missing or needs modifications.
### Thanks!
