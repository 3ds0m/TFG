package com.edson.gonzales.aff.DTO;
import com.edson.gonzales.aff.Entity.Offer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferDTO {
    private int id;
    private Double old_price;
    private Double new_price;
    private String title;
    private String description;
    private String image; // Imagen como base64
    private Double percent;
    private String locationName;
    private LocalDate inicioOferta;
    private LocalDate finOferta;

    // Constructor que convierte la entidad Offer en DTO
    public OfferDTO(Offer offer) {
        this.id = offer.getId();
        this.old_price = offer.getOld_price();
        this.new_price = offer.getNew_price();
        this.title = offer.getTitle();
        this.description = offer.getDescription();
        this.percent = offer.getPercent();
        this.locationName = offer.getLocationName();
        this.image = offer.getImage();
        this.inicioOferta = offer.getInicioOferta();
        this.finOferta = offer.getFinOferta();
    }
}
