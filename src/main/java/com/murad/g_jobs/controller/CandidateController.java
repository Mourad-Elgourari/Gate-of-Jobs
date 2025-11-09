package com.murad.g_jobs.controller;

import com.murad.g_jobs.model.Candidate;
import com.murad.g_jobs.model.enums.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Controller
@RequestMapping("/candidate")
@RequiredArgsConstructor
public class CandidateController {

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("candidate", new Candidate());
        model.addAttribute("roles", Arrays.asList(Role.values()));
        return "candidate/candidate-form";
    }

}
