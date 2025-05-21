package com.edson.gonzales.aff.Controller;

import com.edson.gonzales.aff.DTO.LocationDTO;
import com.edson.gonzales.aff.Entity.Location;
import com.edson.gonzales.aff.Entity.Offer;
import com.edson.gonzales.aff.Repository.LocationRepository;
import com.edson.gonzales.aff.Repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Controller
public class OfferController {
    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private LocationRepository locationRepository;

    @GetMapping("/Ofertas")
    public String ActiveOffers(Model model) {
        model.addAttribute("offers", offerRepository.findAll());
        model.addAttribute("offer", new Offer());
        model.addAttribute("locations", locationRepository.findAll());
        return "Ofertas";
    }

    @PostMapping("/delete/{id}")
    public String deleteOffer(@PathVariable Long id) {
        Optional<Offer> optionalOffer = offerRepository.findById(id);
        if (optionalOffer.isPresent()) {
            Offer offer = optionalOffer.get();
            System.out.println("Offer ID: " + offer.getId());

            // Primero, eliminar la relación en la tabla intermedia
            Location location = offer.getLocation();  // Aquí obtenemos la location relacionada con esta oferta
            if (location != null) {
                System.out.println("Location ID: " + location.getLocationId());
                // Eliminar la oferta de la relación de la location
                location.getOffers().remove(offer);  // Quitar la oferta de las ubicaciones
                locationRepository.save(location);  // Guardar el cambio en la location
            }

            // Ahora eliminar la oferta
            offerRepository.delete(offer);
        }
        return "redirect:/Ofertas";  // Redirigir a la lista de ofertas
    }


    // 🔸 POST - Procesar formulario
    @PostMapping("/create")
    public String createOffer(@ModelAttribute Offer offer,
                              @RequestParam("locationId") String locationId) throws IOException {


        double old_price = offer.getOld_price();
        double new_price = offer.getNew_price();

        if (old_price != 0) {
            double percent = 100 - ((new_price / old_price) * 100);
            percent = Math.round(percent * 100.0) / 100.0;
            offer.setPercent(percent);
        } else {
            offer.setPercent(0.0);
        }

        Location location = locationRepository.findById(locationId).orElse(null);
        if (location != null) {
            offer.setLocation(location); // ✅ ya no es una lista
            offer.setLocationName(location.getName());
            location.getOffers().add(offer);
        }

        offerRepository.save(offer);

        return "redirect:/Ofertas";
    }


    // Método para búsqueda de Location, si es necesario para autocompletar
    @GetMapping("/api/locations/search")
    @ResponseBody
    public List<LocationDTO> searchLocations(@RequestParam("query") String query) {
        return locationRepository.searchByName(query);
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Offer offer = offerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("offer", offer);
        return "Edit"; // Tu plantilla de edición
    }
    @PostMapping("/update")
    public String updateOffer(@ModelAttribute Offer offer,
                              @RequestParam("locationId") String locationId) {

        Offer existingOffer = offerRepository.findById((long) offer.getId())
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + offer.getId()));

        existingOffer.setTitle(offer.getTitle());
        existingOffer.setOld_price(offer.getOld_price());
        existingOffer.setNew_price(offer.getNew_price());
        existingOffer.setDescription(offer.getDescription());
        existingOffer.setInicioOferta(offer.getInicioOferta());
        existingOffer.setFinOferta(offer.getFinOferta());
        existingOffer.setImage(offer.getImage());

        double old_price = offer.getOld_price();
        double new_price = offer.getNew_price();
        double percent = old_price != 0 ? 100 - ((new_price / old_price) * 100) : 0;
        percent = Math.round(percent * 100.0) / 100.0;
        existingOffer.setPercent(percent);

        if (locationId != null) {
            Location location = locationRepository.findById(locationId).orElse(null);
            if (location != null) {
                existingOffer.setLocation(location);
                existingOffer.setLocationName(location.getName());
            }
        }

        offerRepository.save(existingOffer);

        return "redirect:/Ofertas";
    }

}
