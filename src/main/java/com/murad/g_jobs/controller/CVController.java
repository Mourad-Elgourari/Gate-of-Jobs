package com.murad.g_jobs.controller;

import com.murad.g_jobs.model.CV;
import com.murad.g_jobs.model.Candidate;
import com.murad.g_jobs.model.User;
import com.murad.g_jobs.model.enums.Visibility;
import com.murad.g_jobs.repository.CVRepository;
import com.murad.g_jobs.repository.CandidateRepository;
import com.murad.g_jobs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/candidate")
@RequiredArgsConstructor
public class CVController {

    private final String CV_UPLOAD_DIR = "uploads/cvs/";
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final CVRepository cvRepository;

    @PostMapping("/cv/upload")
    public String uploadCV(@RequestParam("cvFile") MultipartFile cvFile,
                           @RequestParam("title") String title,
                           @AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes) {

        try {
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Candidate candidate = user.getCandidate();
            if (candidate == null) {
                redirectAttributes.addFlashAttribute("error", "Please complete your profile first.");
                return "redirect:/candidate/new";
            }

            if (cvFile.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Please select a file to upload.");
                return "redirect:/candidate/new";
            }

            String fileName = System.currentTimeMillis() + "_" + cvFile.getOriginalFilename();
            Path uploadPath = Paths.get(CV_UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(cvFile.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);

            CV cv = CV.builder()
                    .candidate(candidate)
                    .title(title)
                    .fileName(fileName)
                    .visibility(Visibility.PRIVATE)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            cvRepository.save(cv);

            redirectAttributes.addFlashAttribute("success", "CV uploaded successfully!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error uploading CV: " + e.getMessage());
        }

        return "redirect:/candidate/new";
    }

    @GetMapping("/cv/download/{cvId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadCV(@PathVariable Long cvId) throws IOException {
        CV cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new RuntimeException("CV not found"));

        Path filePath = Paths.get(CV_UPLOAD_DIR).resolve(cv.getFileName());
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Could not read file: " + cv.getFileName());
        }

        // Force download with PDF extension
        String downloadFileName = cv.getTitle().replaceAll("\\s+", "_") + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadFileName + "\"")
                .body(resource);
    }
    @PostMapping("/cv/update/{cvId}")
    public String updateCV(@PathVariable Long cvId,
                           @RequestParam("title") String title,
                           @RequestParam("visibility") Visibility visibility,
                           @AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes) {
        try {
            CV cv = cvRepository.findById(cvId)
                    .orElseThrow(() -> new RuntimeException("CV not found"));

            // Ensure logged-in user owns the CV
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!cv.getCandidate().getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "You are not allowed to update this CV.");
                return "redirect:/candidate/new";
            }

            // Update fields
            cv.setTitle(title);
            cv.setVisibility(visibility);

            cvRepository.save(cv);
            redirectAttributes.addFlashAttribute("success", "CV updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating CV: " + e.getMessage());
        }

        return "redirect:/candidate/new";
    }


}
