package com.edson.gonzales.aff.Repository;

import com.edson.gonzales.aff.Entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import com.edson.gonzales.aff.Entity.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    //Filtra por el precio
    List<Restaurant> findByPriceLevel(String priceLevel);
    // Buscar restaurantes por calificación mínima
    List<Restaurant> findByRatingGreaterThanEqual(Double rating);
}
