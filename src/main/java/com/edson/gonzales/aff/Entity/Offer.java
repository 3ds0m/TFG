package com.edson.gonzales.aff.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name="Offers")
@EqualsAndHashCode(exclude = "location")
public class Offer {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private Double old_price;
    @Column
    private Double new_price;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private String image;
    @Column
    private Double percent;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})  // Esto asegura que se eliminen las relaciones al eliminar la oferta
    @JoinColumn(name = "location_id", nullable = false)  // Define la columna location_id
    private Location location;
    // Columna adicional para guardar el nombre del lugar
    @Column(name = "location_name")
    private String locationName;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate InicioOferta;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate FinOferta;
}
