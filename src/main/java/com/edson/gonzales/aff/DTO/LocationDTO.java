package com.edson.gonzales.aff.DTO;

import com.edson.gonzales.aff.Entity.Location;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class LocationDTO {
    private String locationId;
    private String name;
    private String distance;
    private String bearing;
    private String street1;
    private String street2;
    private String city;
    private String country;
    private String postalcode;
    private String addressString;
    private Double latitude;
    private Double longitude;
    private String phoneNumber;
    private Double rating;
    private Integer reviewCount;
    private String image;
    private String priceLevel;
    private String Cuisine_type;
    private List<OfferDTO> offers;

    public LocationDTO(Location location) {
        this.locationId = location.getLocationId();
        this.name = location.getName();
        this.distance = location.getDistance();
        this.bearing = location.getBearing();
        this.street1 = location.getStreet1();
        this.street2 = location.getStreet2();
        this.city = location.getCity();
        this.country = location.getCountry();
        this.postalcode = location.getPostalcode();
        this.addressString = location.getAddressString();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.phoneNumber = location.getPhoneNumber();
        this.rating = location.getRating();
        this.reviewCount = location.getReviewCount();
        this.image = location.getImage();
        this.priceLevel = location.getPriceLevel();
        this.Cuisine_type = location.getCuisine_type();
        // Mapear las ofertas
        this.offers = location.getOffers().stream()
                .map(OfferDTO::new)  // Mapear cada oferta a un OfferDTO
                .collect(Collectors.toList());
    }
    public LocationDTO(String locationId, String name) {
        this.locationId = locationId;
        this.name = name;
    }
    @JsonCreator
    public LocationDTO(@JsonProperty("locationId") String locationId,
                       @JsonProperty("name") String name,
                       // Define otros campos aquí
                       @JsonProperty("offers") List<OfferDTO> offers) {
        this.locationId = locationId;
        this.name = name;
        // Mapear los otros campos
        this.offers = offers;
    }
}
