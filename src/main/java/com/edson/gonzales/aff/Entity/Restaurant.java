package com.edson.gonzales.aff.Entity;

import jakarta.persistence.*;

import java.util.List;

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
    @ManyToMany
    @JoinTable(
            name = "restaurant_cuisines",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name ="cuisine_id")
    )
    private List<Cuisine> cuisines;
}
