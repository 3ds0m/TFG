package com.edson.gonzales.aff.Controller;
import com.edson.gonzales.aff.DTO.LocationDTO;
import com.edson.gonzales.aff.DTO.OfferDTO;
import com.edson.gonzales.aff.Repository.LocationRepository;
import com.edson.gonzales.aff.Repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/listaofertas")
    @Cacheable("offers")
    public List<OfferDTO> getAllLocationsofertas() {
        return offerRepository.findAll().stream()
                .map(OfferDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/listarestaurantes")
    @Cacheable("locationsAll")
    public List<LocationDTO> getAllLocations() {
        return locationRepository.finalLocations().stream()
                .map(LocationDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/listalowcost")
    @Cacheable("locationsLowCost")
    public List<LocationDTO> getLowCostLocations() {
        return locationRepository.LowcostLocations().stream()
                .map(LocationDTO::new)
                .collect(Collectors.toList());
    }

    //Metodo que usando el nombre del restaurant busca la entidad completa en json para mostrar
    @GetMapping("/locations/search")
    @ResponseBody
    public List<LocationDTO> searchLocationsFront(@RequestParam("query") String query) {
        System.out.println(query);
        return locationRepository.searchByNameFront(query);
    }
}