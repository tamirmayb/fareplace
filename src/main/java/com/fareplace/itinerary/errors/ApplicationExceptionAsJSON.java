package com.fareplace.itinerary.errors;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApplicationExceptionAsJSON {
    String url;
    String message;
}