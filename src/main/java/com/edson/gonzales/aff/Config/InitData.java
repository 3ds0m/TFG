package com.edson.gonzales.aff.Config;

import com.edson.gonzales.aff.Entity.Cuisine;
import com.edson.gonzales.aff.Repository.CuisineRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class InitData {

    private final CuisineRepository cuisineRepository;

    // Inyección de dependencias del repository
    @Autowired
    public InitData(CuisineRepository cuisineRepository) {
        this.cuisineRepository = cuisineRepository;
    }

    @PostConstruct
    public void initCuisine() {
        cuisineRepository.saveAll(Arrays.asList(
                Cuisine.builder().name("Cocina italiana").build(),
                Cuisine.builder().name("Cocina española").build()
        ));
    }
}
