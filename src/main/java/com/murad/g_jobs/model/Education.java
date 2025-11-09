package com.murad.g_jobs.model;

import com.murad.g_jobs.model.enums.EducationLevel;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "educations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relationship with CV
    @ManyToOne
    @JoinColumn(name = "cv_id", nullable = false)
    private CV cv;

    @Column(length = 150, nullable = false)
    private String title; // Diplôme obtenu

    @Column(length = 150, nullable = false)
    private String institution; // École / Université

    @Column(length = 100)
    private String city;

    @Column(length = 50)
    private String country;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate; // null if ongoing

    @Column(name = "ongoing", nullable = false)
    private Boolean ongoing = false; // Formation en cours

    @Enumerated(EnumType.STRING)
    private EducationLevel level; // Niveau d'étude

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String distinction; // Mention obtenue
}
