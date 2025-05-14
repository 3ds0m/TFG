package com.edson.gonzales.aff.Controller;
import com.edson.gonzales.aff.DTO.LocationDTO;
import com.edson.gonzales.aff.DTO.OfferDTO;
import com.edson.gonzales.aff.Entity.Location;
import com.edson.gonzales.aff.Entity.Offer;
import com.edson.gonzales.aff.Repository.LocationRepository;
import com.edson.gonzales.aff.Repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EndpointController {
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    private OfferRepository offerRepository;

    // Método para consultar por ciudad
    @GetMapping("/restaurantes/city/{city}")
    public ResponseEntity<List<LocationDTO>> getLocationsByCity(@PathVariable("city") String city) {
        List<LocationDTO> locations = locationRepository.findByCity(city);

        if (locations.isEmpty()) {
            return ResponseEntity.status(404).body(null);  // No se encontraron datos
        }
        return ResponseEntity.ok(locations);
    }
    @GetMapping("/listarestaurantes")
    public List<LocationDTO> getAllLocations() {
        // Devuelve todas las ubicaciones mapeadas a LocationDTO
        return locationRepository.findAll().stream()
                .map(LocationDTO::new)  // Convierte cada Location a LocationDTO
                .collect(Collectors.toList());
    }
    @GetMapping("/listaofertas")
    public List<OfferDTO> getAllLocationsofertas() {
        return offerRepository.findAll().stream()
                .map(OfferDTO::new)  // Convierte cada Location a LocationDTO
                .collect(Collectors.toList());
    }
    @GetMapping("/exponer")
    public List<Location> getAllLocationsoferta() {
        return locationRepository.findIncompleteLocations();
    }
}