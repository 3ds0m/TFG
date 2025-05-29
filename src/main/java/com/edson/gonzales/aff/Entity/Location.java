package com.edson.gonzales.aff.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name="Locations")
@EqualsAndHashCode(exclude = "offers")
public class Location{
    @Id
    @Column
    private String locationId;
    @Column
    private String name;
    @Column
    private String distance;
    @Column
    private String bearing;
    @Column
    private String street1;
    @Column
    private String street2;
    @Column
    private String city;
    @Column
    private String country;
    @Column
    private String postalcode;
    @Column
    private String addressString;
    //Para Mapa
    @Column
    private Double latitude;
    @Column
    private Double longitude;

    //A partir de aqui es nulo en la bbdd
    @Column
    private String phoneNumber;
    @Column
    private Double rating;
    @Column
    private Integer reviewCount;
    @Column
    private String image;
    @Column
    private String priceLevel;
    @Column
    private String Cuisine_type;
    @OneToMany(mappedBy = "location", cascade = CascadeType.PERSIST)
    private Set<Offer> offers = new HashSet<>();
    @Column
    private Boolean geoValidated;
}