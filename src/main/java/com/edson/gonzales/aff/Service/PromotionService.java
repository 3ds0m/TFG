package com.edson.gonzales.aff.Service;

import com.edson.gonzales.aff.Entity.Promotion;
import com.edson.gonzales.aff.Repository.PromotionRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }
    public List<Promotion> getActivePromotions(LocalDate currentDate1, LocalDate currentDate2) {
        return promotionRepository.findByStartDateBeforeAndEndDateAfter(currentDate1, currentDate2);
    }
}
