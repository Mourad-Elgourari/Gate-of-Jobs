package com.murad.g_jobs.controller;

import com.murad.g_jobs.model.Company;
import com.murad.g_jobs.model.enums.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("company", new Company());
        model.addAttribute("roles", Arrays.asList(Role.values()));
        return "company/company-form";
    }
}
