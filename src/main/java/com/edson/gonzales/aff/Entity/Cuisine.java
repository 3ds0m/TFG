package com.edson.gonzales.aff.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cuisines")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Cuisine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
}
