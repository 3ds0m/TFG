package com.edson.gonzales.aff.Repository;

import com.edson.gonzales.aff.Entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OfferRepository extends JpaRepository<Offer,Long> {
    @Query("SELECT o FROM Offer o WHERE o.FinOferta >= :date")
    List<Offer> findByFinOfertaAfterOrEquals(@Param("date") LocalDate date);
}
