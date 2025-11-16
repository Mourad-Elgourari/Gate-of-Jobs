package com.murad.g_jobs.controller;

import com.murad.g_jobs.dto.JobOfferRequest;
import com.murad.g_jobs.model.Company;
import com.murad.g_jobs.model.JobOffer;
import com.murad.g_jobs.repository.CompanyRepository;
import com.murad.g_jobs.repository.JobOfferRepository;
import com.murad.g_jobs.service.JobOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/joboffers")
@RequiredArgsConstructor
public class JobOfferController {

    private final JobOfferService jobOfferService;
    private final JobOfferRepository jobOfferRepository;
    private final CompanyRepository companyRepository;

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public JobOffer createJobOffer(@RequestBody JobOfferRequest request, Authentication auth) {
        return jobOfferService.createJobOffer(request, auth);
    }
    @GetMapping
    @PreAuthorize("hasRole('COMPANY')")
    public String listCompanyOffers(Model model, Authentication auth) {

        String email = auth.getName();

        Company company = companyRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        List<JobOffer> offers = jobOfferRepository.findByCompany(company);

        model.addAttribute("offers", offers);
        return "company/joboffers";
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('COMPANY')")
    public String getOfferDetails(@PathVariable Long id, Model model, Authentication auth) {

        // Only the company who created the offer can view it
        JobOffer offer = jobOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        model.addAttribute("offer", offer);
        return "company/offer-details"; // This is the Thymeleaf page
    }


}
