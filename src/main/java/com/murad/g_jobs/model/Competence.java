package com.murad.g_jobs.model;

import com.murad.g_jobs.model.enums.CompetenceCategory;
import com.murad.g_jobs.model.enums.CompetenceLevel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "competences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Competence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one with CV
    @ManyToOne
    @JoinColumn(name = "cv_id", nullable = false)
    private CV cv;

    @Column(length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompetenceLevel level;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompetenceCategory category;

    private Integer yearsOfExperience;
}
