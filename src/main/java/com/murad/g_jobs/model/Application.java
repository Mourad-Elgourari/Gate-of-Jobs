package com.murad.g_jobs.model;

import com.murad.g_jobs.model.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidatures") // keep table name in French
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "offre_id", nullable = false)
    private JobOffer jobOffer;

    @ManyToOne
    @JoinColumn(name = "cv_id")
    private CV cv;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime applicationDate;

    @Column(columnDefinition = "TEXT")
    private String motivationLetter;

    @Column(columnDefinition = "TEXT")
    private String recruiterNotes;

    private LocalDateTime responseDate;
}
