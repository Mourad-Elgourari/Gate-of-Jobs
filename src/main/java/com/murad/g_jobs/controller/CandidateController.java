package com.murad.g_jobs.controller;

import com.murad.g_jobs.model.Candidate;
import com.murad.g_jobs.model.Company;
import com.murad.g_jobs.model.User;
import com.murad.g_jobs.model.enums.Role;
import com.murad.g_jobs.repository.CandidateRepository;
import com.murad.g_jobs.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
@Controller
@RequestMapping("/candidate")
@RequiredArgsConstructor
public class CandidateController {

    private final UserRepository  userRepository;
    private final CandidateRepository candidateRepository;

    private final String UPLOAD_DIR = "uploads/candidate-photos/";

    // --- Global model attribute for authenticated candidate ---
    @ModelAttribute("candidate")
    public Candidate authenticatedCandidate(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElse(null);
            if (user != null) {
                return user.getCandidate();
            }
        }
        return null;
    }

    @GetMapping("/new")
    public String newCandidate(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Candidate candidate = user.getCandidate() != null ? user.getCandidate() : new Candidate();
        model.addAttribute("candidate", candidate);

        return "candidate/candidate-form"; // Ensure file name matches
    }

    @PostMapping("/profile/save")
    public String saveCandidate(@ModelAttribute("candidate") Candidate formCandidate,
                                @RequestParam("photoFile") MultipartFile photoFile,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes) {

        try {
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Candidate candidateEntity = user.getCandidate() != null
                    ? user.getCandidate()
                    : formCandidate;

            candidateEntity.setUser(user);

            candidateEntity.setFirstName(formCandidate.getFirstName());
            candidateEntity.setLastName(formCandidate.getLastName());
            candidateEntity.setPhone(formCandidate.getPhone());
            candidateEntity.setAddress(formCandidate.getAddress());
            candidateEntity.setCity(formCandidate.getCity());
            candidateEntity.setPostalCode(formCandidate.getPostalCode());
            candidateEntity.setCountry(formCandidate.getCountry());
            candidateEntity.setBirthDate(formCandidate.getBirthDate());
            candidateEntity.setLinkedinUrl(formCandidate.getLinkedinUrl());
            candidateEntity.setGithubUrl(formCandidate.getGithubUrl());
            candidateEntity.setPortfolioUrl(formCandidate.getPortfolioUrl());

            if (!photoFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Files.copy(photoFile.getInputStream(),
                        uploadPath.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING);

                candidateEntity.setPhoto(fileName);
            }

            candidateRepository.save(candidateEntity);
            redirectAttributes.addFlashAttribute("success", "Profile saved successfully!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving profile: " + e.getMessage());
        }

        return "redirect:/candidate/new";
    }

    @GetMapping("/profile/photo/{id}")
    @ResponseBody
    public byte[] getPhoto(@PathVariable Long id) throws IOException {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        if (candidate.getPhoto() == null) return new byte[0];

        Path photoPath = Paths.get(UPLOAD_DIR).resolve(candidate.getPhoto());
        return Files.readAllBytes(photoPath);
    }
}
