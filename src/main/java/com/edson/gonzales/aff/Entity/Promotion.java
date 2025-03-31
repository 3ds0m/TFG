package com.edson.gonzales.aff.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String discount;
    private String startDate;
    private String endDate;
}
