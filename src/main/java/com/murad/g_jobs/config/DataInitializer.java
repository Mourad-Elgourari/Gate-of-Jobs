package com.murad.g_jobs.config;

import com.murad.g_jobs.model.User;
import com.murad.g_jobs.model.enums.Role;
import com.murad.g_jobs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Créer un utilisateur admin par défaut s'il n'existe pas
        if (!userRepository.existsByEmail("admin@example.com")) {
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123")); // Mot de passe par défaut
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
            System.out.println("✅ Utilisateur admin créé : username='admin', password='admin123'");
        } else {
            System.out.println("ℹ️  Utilisateur admin existe déjà.");
        }
    }
}
