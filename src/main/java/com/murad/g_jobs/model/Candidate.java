package com.murad.g_jobs.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "candidates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One-to-one with User
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

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

    private LocalDate birthDate;

    @Column(length = 255)
    private String photo;

    @Column(length = 255)
    private String linkedinUrl;

    @Column(length = 255)
    private String githubUrl;

    @Column(length = 255)
    private String portfolioUrl;

    // One-to-many relationships
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CV> cvs;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;
}

