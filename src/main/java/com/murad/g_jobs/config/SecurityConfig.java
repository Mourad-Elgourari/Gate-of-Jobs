package com.murad.g_jobs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/login", "/register", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/dashboard/**").authenticated()
                        .requestMatchers("/user/**").hasRole("ADMIN")
                        .requestMatchers("/candidate/**").hasRole("CANDIDATE")
                        .requestMatchers("/company/**").hasRole("COMPANY")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureHandler((request, response, exception) -> {
                            exception.printStackTrace(); // 👈 see reason in console
                            response.sendRedirect("/login?error");
                        })
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/")  // redirect if invalid
                        .maximumSessions(1)
                        .expiredUrl("/")          // redirect if expired
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            // 👇 if session expired or user not authenticated
                            if (request.getRequestURI().startsWith("/dashboard") || request.getRequestURI().startsWith("/user")) {
                                response.sendRedirect("/"); // redirect to home page
                            } else {
                                response.sendRedirect("/login");
                            }
                        })
                );

        return http.build();
    }
}
