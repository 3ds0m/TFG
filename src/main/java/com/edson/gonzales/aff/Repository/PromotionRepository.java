package com.edson.gonzales.aff.Repository;


import com.edson.gonzales.aff.Entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion,Long> {
    List<Promotion> findAll();
    List<Promotion>findByStartDateBeforeAndEndDateAfter(LocalDate currentDate, LocalDate currentDate2);
}
