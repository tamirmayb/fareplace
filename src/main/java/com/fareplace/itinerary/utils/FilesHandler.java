package com.fareplace.itinerary.utils;

import java.io.File;
import java.util.Objects;

public class FilesHandler {
    public static File readFile(String fileName) {
        ClassLoader classLoader = FilesHandler.class.getClassLoader();
        return new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
    }
}
