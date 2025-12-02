package com.murad.g_jobs.config;

import com.murad.g_jobs.model.User;
import com.murad.g_jobs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final UserRepository userRepository;

    @ModelAttribute
    public void addCommonAttributes(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return;
        }

        String email = authentication.getName();
        var userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) return;

        User user = userOpt.get();

        model.addAttribute("company", user.getCompany());
        model.addAttribute("candidate", user.getCandidate());
    }
}
