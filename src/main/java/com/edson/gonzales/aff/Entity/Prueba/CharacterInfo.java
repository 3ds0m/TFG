package com.edson.gonzales.aff.Entity.Prueba;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class CharacterInfo {
    private int id;
    private String name;
    private String status;
    private String species;
    private String gender;
    private Origin origin;
    private Location location;
    private String image;
    private List<String> episode;
}
