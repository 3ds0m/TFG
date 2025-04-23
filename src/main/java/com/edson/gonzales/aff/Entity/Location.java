package com.edson.gonzales.aff.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name="Locations")
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

    public Location() {

    }
}
