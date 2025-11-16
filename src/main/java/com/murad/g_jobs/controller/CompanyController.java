package com.murad.g_jobs.controller;

import com.murad.g_jobs.model.Company;
import com.murad.g_jobs.model.JobOffer;
import com.murad.g_jobs.model.User;
import com.murad.g_jobs.repository.CompanyRepository;
import com.murad.g_jobs.repository.JobOfferRepository;
import com.murad.g_jobs.repository.UserRepository;
import com.murad.g_jobs.service.JobOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JobOfferService jobOfferService;

    private final String UPLOAD_DIR = "uploads/company-logos/";

    /** Show form for new or existing company */
    @GetMapping("/profile/new")
    public String newCompanyForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Company company = user.getCompany() != null ? user.getCompany() : new Company();
        model.addAttribute("company", company);

        return "company/company-form";
    }

    /** Save or update company */
    @PostMapping("/profile/save")
    public String saveCompany(@ModelAttribute("company") Company company,
                              @RequestParam("logoFile") MultipartFile logoFile,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {

        try {
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Fetch existing company if any
            Company existingCompany = user.getCompany();
            if (existingCompany != null) {
                company.setId(existingCompany.getId()); // set ID so JPA updates instead of inserts
            }

            // Handle logo upload
            if (!logoFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + logoFile.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                try (InputStream inputStream = logoFile.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    company.setLogo(fileName);
                }
            } else if (existingCompany != null) {
                // Keep existing logo if no new file uploaded
                company.setLogo(existingCompany.getLogo());
            }

            company.setUser(user);
            companyRepository.save(company);

            redirectAttributes.addFlashAttribute("success", "Company saved successfully!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error uploading logo: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving company: " + e.getMessage());
        }

        return "redirect:/company/profile/new";
    }


    /** Serve company logo */
    @GetMapping("/logo/{id}")
    @ResponseBody
    public byte[] getLogo(@PathVariable Long id) throws IOException {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        if (company.getLogo() == null) {
            return new byte[0];
        }
        Path logoPath = Paths.get(UPLOAD_DIR).resolve(company.getLogo());
        return Files.readAllBytes(logoPath);
    }
    @GetMapping("/joboffers/add")
    @PreAuthorize("hasRole('COMPANY')")
    public String showAddOfferForm(Model model) {
        model.addAttribute("jobOffer", new JobOffer());
        return "company/joboffer-form";
    }
    @PostMapping("/joboffers/save")
    @PreAuthorize("hasRole('COMPANY')")
    public String saveOffer(@ModelAttribute("jobOffer") JobOffer offer,
                            Authentication auth,
                            RedirectAttributes redirectAttributes) {

        try {
            jobOfferService.createOfferFromForm(offer, auth);
            redirectAttributes.addFlashAttribute("success", "Job offer added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/api/joboffers";
    }


}
