package com.edson.gonzales.aff.Controller;

import com.edson.gonzales.aff.DTO.ScrapperRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ScrapperRestController {

    private static final String BASE_URL = "https://api.content.tripadvisor.com/api/v1/location/search";

    @PostMapping("/scraper/start")
    public List<String> startScraping(@RequestBody ScrapperRequest request) {
        List<String> urls = new ArrayList<>();

        String[] categories = request.getCategories().split(",");

        for (ScrapperRequest.Point point : request.getPoints()) {
            for (String category : categories) {
                category = category.trim();
                if(category.isEmpty()) continue;
                String url = BASE_URL +
                        "?key=" + request.getApiKey() +
                        "&searchQuery=" + category +
                        "&category=restaurant" +
                        "&latLong=" + point.getLat() + "," + point.getLon() +
                        "&radius=" + request.getRadiusKm() +
                        "&radiusUnit=km" +
                        "&language=es";
                urls.add(url);
            }
        }
        // Guarda en archivo ./Requests/request.txt
        try (PrintWriter writer = new PrintWriter(new FileWriter("./Requests/request.txt"))) {
            for (String url : urls) {
                writer.println(url);
            }
        } catch (IOException e) {
            e.printStackTrace(); // opcional: loguear
        }

        System.out.println("Links generados y guardados: " + urls.size());
        return urls;
    }
    @GetMapping("/")
    public ModelAndView scrapperPage() {
        return new ModelAndView("DataFinder.html");
    }
}