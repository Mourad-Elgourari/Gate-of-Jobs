package com.murad.g_jobs.model;

import com.murad.g_jobs.model.enums.CompetenceLevel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "competences_requises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequiredSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "offre_id", nullable = false)
    private JobOffer jobOffer;

    @Column(length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompetenceLevel level;

    @Column(nullable = false)
    private Boolean required = true;
}
