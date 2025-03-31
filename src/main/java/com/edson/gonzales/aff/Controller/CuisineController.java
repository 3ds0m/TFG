package com.edson.gonzales.aff.Controller;

import com.edson.gonzales.aff.Repository.CuisineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CuisineController {
    private final CuisineRepository cuisineRepository;

    @GetMapping("/cuisines")
    public String cuisines(Model model) {
        model.addAttribute("cuisines", cuisineRepository.findAll());
        System.out.println("Datos insertados: " + cuisineRepository.findAll());
        return "cuisines";
    }

}
