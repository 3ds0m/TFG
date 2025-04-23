package com.edson.gonzales.aff.Controller;
import com.edson.gonzales.aff.DTO.LocationDTO;
import com.edson.gonzales.aff.Entity.Location;
import com.edson.gonzales.aff.Repository.LocationRepository;
import com.edson.gonzales.aff.Service.RPAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EndpointController {
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    RPAService rpaService;
    // Método para consultar por ciudad
    @GetMapping("/restaurantes/city/{city}")
    public ResponseEntity<List<Location>> getLocationsByCity(@PathVariable String city) {
        List<Location> locations = locationRepository.findByCity(city);

        if (locations.isEmpty()) {
            return ResponseEntity.status(404).body(null);  // No se encontraron datos
        }
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/RPA")
    public ResponseEntity<String> getLocationsByRPA() {
        try {
            rpaService.completarDatosDesdeGoogleMaps();
            return ResponseEntity.ok("Datos completados exitosamente desde Google Maps.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ocurrió un error al ejecutar el RPA.");
        }
    }
}