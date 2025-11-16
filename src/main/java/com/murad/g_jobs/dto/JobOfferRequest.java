package com.murad.g_jobs.dto;

import com.murad.g_jobs.model.enums.ContractType;
import com.murad.g_jobs.model.enums.ExperienceRequired;
import com.murad.g_jobs.model.enums.EducationLevel;
import lombok.Data;

import java.time.LocalDate;

@Data
public class JobOfferRequest {

    private String title;
    private String description;
    private String missions;
    private String profile;
    private ContractType contractType;
    private String city;
    private String postalCode;
    private String country;
    private String salary;
    private ExperienceRequired experienceRequired;
    private EducationLevel educationLevel;
    private LocalDate expirationDate;
    private Integer numberOfPositions;
    private Boolean remote;
}
