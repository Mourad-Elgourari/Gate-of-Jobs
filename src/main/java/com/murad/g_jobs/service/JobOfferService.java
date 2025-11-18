package com.murad.g_jobs.service;

import com.murad.g_jobs.dto.JobOfferRequest;
import com.murad.g_jobs.model.Company;
import com.murad.g_jobs.model.JobOffer;
import com.murad.g_jobs.repository.CompanyRepository;
import com.murad.g_jobs.repository.JobOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final CompanyRepository companyRepository;

    public JobOffer createJobOffer(JobOfferRequest request, Authentication auth) {

        // Récupérer l’entreprise connectée
        String email = auth.getName();
        Company company = companyRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        JobOffer offer = JobOffer.builder()
                .company(company)
                .title(request.getTitle())
                .description(request.getDescription())
                .missions(request.getMissions())
                .profile(request.getProfile())
                .contractType(request.getContractType())
                .city(request.getCity())
                .postalCode(request.getPostalCode())
                .country(request.getCountry() != null ? request.getCountry() : "France")
                .salary(request.getSalary())
                .experienceRequired(request.getExperienceRequired())
                .educationLevel(request.getEducationLevel())
                .publicationDate(LocalDateTime.now())
                .expirationDate(request.getExpirationDate())
                .numberOfPositions(request.getNumberOfPositions() != null ? request.getNumberOfPositions() : 1)
                .remote(request.getRemote() != null ? request.getRemote() : false)
                .active(true)
                .build();

        return jobOfferRepository.save(offer);
    }
    public JobOffer createOfferFromForm(JobOffer offer, Authentication auth) {

        String email = auth.getName();

        Company company = companyRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        offer.setCompany(company);
        offer.setPublicationDate(LocalDateTime.now());

        if (offer.getNumberOfPositions() == null)
            offer.setNumberOfPositions(1);

        if (offer.getRemote() == null)
            offer.setRemote(false);

        // Set active based on expiration date
        if (offer.getExpirationDate() != null && offer.getExpirationDate().isBefore(java.time.LocalDate.now())) {
            offer.setActive(false);
        } else {
            offer.setActive(true);
        }

        return jobOfferRepository.save(offer);
    }


}
