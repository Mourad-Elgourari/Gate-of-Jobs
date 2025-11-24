package com.murad.g_jobs.controller;

import com.murad.g_jobs.model.JobOffer;
import com.murad.g_jobs.model.enums.Role;
import com.murad.g_jobs.repository.CandidateRepository;
import com.murad.g_jobs.repository.CompanyRepository;
import com.murad.g_jobs.repository.JobOfferRepository;
import com.murad.g_jobs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final CompanyRepository companyRepository;
    private final JobOfferRepository jobOfferRepository;

    @GetMapping
    public String index(Model model, Authentication authentication) {
        // Get current logged-in user
        String email = authentication.getName();
        var user = userRepository.findByEmail(email).orElse(null);

        // Add company to model if exists
        if (user != null && user.getCompany() != null) {
            model.addAttribute("company", user.getCompany());
        }

        // Admin dashboard
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("userCount", userRepository.count());
            model.addAttribute("candidateCount", candidateRepository.count());
            model.addAttribute("companyCount", companyRepository.count());
            return "dashboard/dashboard";
        }

        // Company dashboard
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {

            var company = user.getCompany();
            model.addAttribute("companyData", company); // current company

            // Get list of posted offers for the logged-in company
            List<JobOffer> postedOffers = jobOfferRepository.findByCompany(company);
            model.addAttribute("postedOffers", postedOffers);

            // Optionally, count them
            model.addAttribute("jobOfferCount", postedOffers.size());

            return "company/dashboard";
        }

        // Candidate dashboard
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {

            var candidate = user.getCandidate();  // get current candidate
            model.addAttribute("candidate", candidate);  // use the same name as navbar expects

            return "candidate/dashboard";
        }

        return "redirect:/login";
    }

}
