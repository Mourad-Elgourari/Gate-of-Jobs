package com.murad.g_jobs.controller;

import com.murad.g_jobs.model.enums.Role;
import com.murad.g_jobs.repository.CandidateRepository;
import com.murad.g_jobs.repository.CompanyRepository;
import com.murad.g_jobs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final CompanyRepository companyRepository;

    @GetMapping
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // Admin dashboard data
            model.addAttribute("userCount", userRepository.count());
            model.addAttribute("candidateCount", candidateRepository.count());
            model.addAttribute("companyCount", companyRepository.count());
            return "dashboard/dashboard";
        }

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
            // Company dashboard data
            model.addAttribute("companyData", companyRepository.findAll()); // example
            return "company/dashboard";
        }

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
            // Candidate dashboard data
            model.addAttribute("candidateData", candidateRepository.findAll()); // example
            return "candidate/dashboard";
        }

        // Default fallback if no roles match
        return "redirect:/login";
    }
}
