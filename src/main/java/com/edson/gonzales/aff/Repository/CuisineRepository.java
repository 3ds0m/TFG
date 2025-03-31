package com.edson.gonzales.aff.Repository;

import com.edson.gonzales.aff.Entity.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuisineRepository extends JpaRepository<Cuisine, Long> {
    List<Cuisine> findAll();
    Optional<Cuisine> findByName(String name);
}
