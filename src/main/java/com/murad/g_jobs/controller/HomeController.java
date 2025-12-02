package com.murad.g_jobs.controller;

import com.murad.g_jobs.repository.JobOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final JobOfferRepository jobOfferRepository;

    @GetMapping("/")
    public String home(Model model, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            // Guest → show job offers on home page
            model.addAttribute("offers", jobOfferRepository.findAll());
            return "home"; // home.html
        }

        // Logged-in → redirect to dashboard
        return "redirect:/dashboard";
    }

}
