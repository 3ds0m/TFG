package com.edson.gonzales.aff.Controller.Prueba;

import com.edson.gonzales.aff.Service.Prueba.RickandMortyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/prueba")
@RequiredArgsConstructor

public class RickAndMortyController {
    private final RickandMortyService service;
    @GetMapping("/character/{id}")
    public String fetchCharacter(@PathVariable int id, Model model) {
        service.fetchAndSaveCharacter(id);
        return "Datos del personaje guardados en Json";
    }
}
