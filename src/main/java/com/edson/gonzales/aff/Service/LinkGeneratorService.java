package com.edson.gonzales.aff.Service;

import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LinkGeneratorService {

    private static final String BASE_URL = "https://api.content.tripadvisor.com/api/v1/location/search";
    private static final String API_KEY = "746F3ABE44B944CDA5DCFF366DDFD396";
    private static final String[] CATEGORIES = {
            "casera", "cocteleria", "bar", "restaurante", "chocolateria", "kebab"
    };

    // Límites aproximados de Madrid
    private static final double LAT_START = 40.35;
    private static final double LAT_END = 40.50;
    private static final double LON_START = -3.80;
    private static final double LON_END = -3.60;
    private static final double LAT_STEP = 0.0135;
    private static final double LON_STEP = 0.0180;

    public int generateTripadvisorUrls(String outputPath) throws IOException {
        List<String> urls = new ArrayList<>();

        double currentLat = LAT_START;
        while (currentLat <= LAT_END) {
            double currentLon = LON_START;
            while (currentLon <= LON_END) {
                for (String category : CATEGORIES) {
                    String url = BASE_URL +
                            "?key=" + API_KEY +
                            "&searchQuery=" + category +
                            "&category=restaurant" +
                            "&latLong=" + currentLat + "," + currentLon +
                            "&radius=1" +
                            "&radiusUnit=km" +
                            "&language=es";
                    urls.add(url);
                }
                currentLon += LON_STEP;
            }
            currentLat += LAT_STEP;
        }

        try (FileWriter writer = new FileWriter(outputPath)) {
            for (String url : urls) {
                writer.write(url + "\n");
            }
        }

        return urls.size();
    }
}
