package com.edson.gonzales.aff.Repository;

import com.edson.gonzales.aff.DTO.LocationDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import com.edson.gonzales.aff.Entity.Location;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByCity(String city);
    Optional<Location> findByLocationId(String locationId);
    @Query("SELECT new com.edson.gonzales.aff.DTO.LocationDTO(l.locationId, l.name) " +
            "FROM Location l " +
            "WHERE l.phoneNumber IS NULL " +
            "AND l.rating IS NULL " +
            "AND l.reviewCount IS NULL " +
            "AND l.image IS NULL " +
            "AND l.priceLevel IS NULL " +
            "AND l.Cuisine_type IS NULL" +
            " AND l.image is NULL")
    List<LocationDTO> findLocationsWhereExtraFieldsAreNull();

    @Query("SELECT l FROM Location l " +
            "WHERE l.latitude IS NULL " +
            "AND l.longitude IS NULL")
    List<Location> findIncompleteLocations();
}
