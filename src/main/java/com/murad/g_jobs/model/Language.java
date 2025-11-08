package com.murad.g_jobs.model;

import com.murad.g_jobs.model.enums.LanguageLevel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "languages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one with CV
    @ManyToOne
    @JoinColumn(name = "cv_id", nullable = false)
    private CV cv;

    @Column(length = 50, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LanguageLevel level;

    @Column(length = 100)
    private String certification;
}
