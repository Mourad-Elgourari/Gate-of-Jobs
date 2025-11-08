package com.murad.g_jobs.controller;

import com.murad.g_jobs.model.User;
import com.murad.g_jobs.model.enums.Role;
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
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String loginRequired, Model model) {
        if (loginRequired != null) {
            model.addAttribute("message", "You must be logged in to make a command.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        // ✅ Only allow CANDIDATE and EMPLOYER to be selectable
        model.addAttribute("roles", Set.of(Role.CANDIDATE, Role.COMPANY));

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("emailExists", true);
            return "register";
        }

        // ✅ Default role if none selected
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole(Set.of(Role.CANDIDATE));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return "redirect:/login?registered";
    }
}
