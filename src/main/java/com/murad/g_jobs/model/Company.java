package com.murad.g_jobs.model;

import jakarta.persistence.*;
import com.murad.g_jobs.model.enums.CompanySize;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One-to-one with User
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "company_name", length = 100, nullable = false)
    private String companyName;

    @Column(name = "activity_sector", length = 100)
    private String activitySector;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "website", length = 255)
    private String website;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(length = 50)
    private String country = "France";

    @Column(length = 255)
    private String logo;

    @Enumerated(EnumType.STRING)
    private CompanySize size; // Enum representing the size of the company

    // One-to-many relationship with job offers
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobOffer> jobOffers;
}

