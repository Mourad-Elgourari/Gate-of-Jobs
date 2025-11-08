package com.murad.g_jobs.model;

import com.murad.g_jobs.model.enums.ContractType;
import com.murad.g_jobs.model.enums.ExperienceRequired;
import com.murad.g_jobs.model.enums.EducationLevel;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "offres_emploi") // keep table name in French
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(length = 150, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String missions;

    @Column(columnDefinition = "TEXT")
    private String profile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractType contractType;

    @Column(length = 100, nullable = false)
    private String city;

    @Column(length = 10)
    private String postalCode;

    @Column(length = 50)
    private String country = "France";

    @Column(length = 50)
    private String salary;

    @Enumerated(EnumType.STRING)
    private ExperienceRequired experienceRequired;

    @Enumerated(EnumType.STRING)
    private EducationLevel educationLevel;

    @Column(nullable = false)
    private LocalDateTime publicationDate;

    private LocalDate expirationDate;

    @Column(nullable = false)
    private Integer numberOfPositions = 1;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private Boolean remote = false;

    @OneToMany(mappedBy = "jobOffer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    @OneToMany(mappedBy = "jobOffer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequiredSkill> requiredSkills;
}
