package com.edson.gonzales.aff.Repository;

import com.edson.gonzales.aff.DTO.LocationDTO;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.edson.gonzales.aff.Entity.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, String> {
    List<LocationDTO>findByCity(String city);
    @Query("SELECT l FROM Location l " +
            "WHERE l.latitude IS NULL " +
            "AND l.longitude IS NULL " )
    List<Location> findIncompleteLocations();

    @Query("SELECT l FROM Location l " +
            "WHERE l.latitude IS NOT NULL " +
            "AND l.longitude IS NOT NULL " +
            "AND l.reviewCount IS NOT NULL ")
    List<Location> finalLocations();

    @Query("SELECT l FROM Location l " +
            "WHERE l.latitude IS NOT NULL " +
            "AND l.longitude IS NOT NULL " +
            "AND l.reviewCount IS NOT NULL " +
            "AND l.priceLevel IN ('$','$$') " +
            "AND l.image != 'N/A' ")
    List<Location> LowcostLocations();

    boolean existsByLocationId(String locationId);

    @Query("SELECT new com.edson.gonzales.aff.DTO.LocationDTO(l.locationId, l.name) FROM Location l WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<LocationDTO> searchByName(@Param("query") String query);

    @Query("SELECT l FROM Location l WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<LocationDTO> searchByNameFront(@Param("query") String query);
}
