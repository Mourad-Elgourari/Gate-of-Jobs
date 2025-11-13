package com.murad.g_jobs.controller;

import com.murad.g_jobs.model.Candidate;
import com.murad.g_jobs.model.Company;
import com.murad.g_jobs.model.User;
import com.murad.g_jobs.model.enums.Role;
import com.murad.g_jobs.repository.CandidateRepository;
import com.murad.g_jobs.repository.CompanyRepository;
import com.murad.g_jobs.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String loginRequired, Model model) {
        if (loginRequired != null) {
            model.addAttribute("message", "You must be logged in to continue.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", new Role[]{Role.CANDIDATE, Role.COMPANY});
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               @RequestParam String role, // read role from form
                               @RequestParam(required = false) String lastName,
                               @RequestParam(required = false) String firstName,
                               @RequestParam(required = false) String companyName,
                               Model model) {

        if (result.hasErrors()) {
            model.addAttribute("roles", new Role[]{Role.CANDIDATE, Role.COMPANY});
            return "register";
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("emailExists", true);
            model.addAttribute("roles", new Role[]{Role.CANDIDATE, Role.COMPANY});
            return "register";
        }

        // Convert role string to enum
        user.setRole(Role.valueOf(role));

        // Encode password and save user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // Save Candidat or Recruteur
        if (savedUser.getRole() == Role.CANDIDATE) {
            if (lastName == null || firstName == null || lastName.isBlank() || firstName.isBlank()) {
                model.addAttribute("error", "Nom et prénom sont obligatoires pour un candidat.");
                model.addAttribute("roles", new Role[]{Role.CANDIDATE, Role.COMPANY});
                return "register";
            }
            Candidate candidate = new Candidate();
            candidate.setUser(savedUser);
            candidate.setLastName(lastName);
            candidate.setFirstName(firstName);
            candidateRepository.save(candidate);
        } else if (savedUser.getRole() == Role.COMPANY) {
            if (companyName == null || companyName.isBlank()) {
                model.addAttribute("error", "Nom de l'entreprise est obligatoire pour un recruteur.");
                model.addAttribute("roles", new Role[]{Role.CANDIDATE, Role.COMPANY});
                return "register";
            }
            Company company = new Company();
            company.setUser(savedUser);
            company.setCompanyName(companyName);
            companyRepository.save(company);
        }

        return "redirect:/login?registered=success";
    }
}
