package com.edson.gonzales.aff.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScrapperRequest {
    private String apiKey;
    private String categories;
    private double radiusKm;
    private double latStart;
    private double latEnd;
    private double lonStart;
    private double lonEnd;
    private double centerLat;
    private double centerLon;

}
