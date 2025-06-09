package com.edson.gonzales.aff.Repository;

import com.edson.gonzales.aff.Entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OfferRepository extends JpaRepository<Offer,Long> {
    List<Offer> findByFinOfertaGreaterThanEqual(LocalDate date);
}
