package com.murad.g_jobs.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "certifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one with CV
    @ManyToOne
    @JoinColumn(name = "cv_id", nullable = false)
    private CV cv;

    @Column(length = 150, nullable = false)
    private String name;

    @Column(length = 150, nullable = false)
    private String organization;

    @Column(name = "obtained_date", nullable = false)
    private LocalDate obtainedDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(length = 100)
    private String number;

    @Column(length = 255)
    private String url;
}
