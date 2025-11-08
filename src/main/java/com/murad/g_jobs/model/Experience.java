package com.murad.g_jobs.model;

import com.murad.g_jobs.model.enums.ContractType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "experiences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relationship with CV
    @ManyToOne
    @JoinColumn(name = "cv_id", nullable = false)
    private CV cv;

    @Column(length = 150, nullable = false)
    private String position; // Intitulé du poste

    @Column(length = 150, nullable = false)
    private String company; // Nom de l'entreprise

    @Column(length = 100)
    private String city;

    @Column(length = 50)
    private String country;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate; // null if ongoing

    @Column(name = "ongoing", nullable = false)
    private Boolean ongoing = false; // Poste actuel

    @Enumerated(EnumType.STRING)
    private ContractType contractType; // Type de contrat

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description; // Missions et responsabilités

    @Column(columnDefinition = "TEXT")
    private String achievements; // Réalisations clés
}
