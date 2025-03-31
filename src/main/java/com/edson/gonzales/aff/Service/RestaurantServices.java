package com.edson.gonzales.aff.Service;

import com.edson.gonzales.aff.Entity.Restaurant;
import com.edson.gonzales.aff.Repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServices {
    private final RestaurantRepository restaurantRepository;
    //Lista basica inicializada
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }
    //Servicio de filtrado referenciando al repositorio que utiliza JPA
    public List<Restaurant> getRestaurantsByPrice(String priceLevel) {
        return restaurantRepository.findByPriceLevel(priceLevel);
    }
    // Buscar restaurantes por calificación mínima
    public List<Restaurant> getRestaurantsByRating(Double minRating) {
        return restaurantRepository.findByRatingGreaterThanEqual(minRating);
    }
}
