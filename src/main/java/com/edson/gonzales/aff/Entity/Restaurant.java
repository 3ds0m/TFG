package com.edson.gonzales.aff.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurantes")
public class Restaurant{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String priceLevel;
    private Double rating;
    private Double latitude;
    private Double longitude;
}
