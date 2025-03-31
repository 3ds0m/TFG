package com.edson.gonzales.aff.Service;

import com.edson.gonzales.aff.Entity.Cuisine;
import com.edson.gonzales.aff.Repository.CuisineRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CuisineService {
    private final CuisineRepository cuisineRepository;

    //Referencia la lista de cocinas declarada en el repositorio de cuisine
    public List<Cuisine> getAllCuisines() {
        return cuisineRepository.findAll();
    }
    //Busca tipos de cocina por nombre
    public Optional<Cuisine> getCuisineByName(String name) {
        return cuisineRepository.findByName(name);
    }
}
