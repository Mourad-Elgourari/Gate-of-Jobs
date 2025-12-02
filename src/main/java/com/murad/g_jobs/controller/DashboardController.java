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
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        // Get current logged-in user
        String email = authentication.getName();
        var userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        var user = userOpt.get();

        // Add company and candidate to model if exist
        if (user.getCompany() != null) {
            model.addAttribute("company", user.getCompany());
            model.addAttribute("companyData", user.getCompany()); // for dashboard
        }
        if (user.getCandidate() != null) {
            model.addAttribute("candidate", user.getCandidate());
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

            // Safety check: redirect to home if company is null
            if (company == null) {
                return "redirect:/";
            }

            // Get list of posted offers for the logged-in company
            List<JobOffer> postedOffers = jobOfferRepository.findByCompany(company);
            model.addAttribute("postedOffers", postedOffers);
            model.addAttribute("jobOfferCount", postedOffers.size());

            return "company/dashboard";
        }

        // Candidate dashboard
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {

            var candidate = user.getCandidate();

            // Safety check: redirect to home if candidate profile missing
            if (candidate == null) {
                return "redirect:/";
            }

            // Load all job offers
            List<JobOffer> offers = jobOfferRepository.findAll();
            model.addAttribute("offers", offers);

            return "candidate/dashboard";
        }

        return "redirect:/login";
    }

}
