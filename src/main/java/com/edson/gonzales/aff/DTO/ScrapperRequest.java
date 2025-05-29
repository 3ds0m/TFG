package com.edson.gonzales.aff.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScrapperRequest {
    private String apiKey;
    private String categories;
    private double radiusKm;
    private double centerLat;
    private double centerLon;
    private List<Point> points;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Point {
        private double lat;
        private double lon;
    }
}
