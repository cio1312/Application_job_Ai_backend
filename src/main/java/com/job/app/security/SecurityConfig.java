package com.job.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    /**
     * Security filter chain configuration that ensures:
     * - Public access to certain endpoints (auth, jobs, H2 console).
     * - Authentication required for all other endpoints.
     * - Default form-based login and logout.
     * - Enables CORS with specific configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                // Public access to auth and job-related endpoints, including reset-password
                .requestMatchers("/api/auth/**", "/api/jobs/**", "/api/auth/reset-password").permitAll()  // Allow access to reset-password
                .requestMatchers("/api/job-applications/apply").authenticated()
                .requestMatchers("/h2-console/**").permitAll()  // Public access to H2 console
                .anyRequest().authenticated()  // All other endpoints require authentication
            .and()
            .formLogin()  // Enables default form-based login
            .and()
            .logout()  // Enables default logout functionality
            .and()
            .headers()
                .frameOptions().sameOrigin()  // Allows H2 console iframe access
            .and()
            .cors()  // Uses custom CORS configuration
            .and()
            .httpBasic();  // Enables HTTP basic authentication (if needed)

        return http.build();
    }

    /**
     * Bean for password encoder. This uses BCrypt hashing algorithm to securely store passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Use bcrypt for password encoding
    }

    /**
     * In-memory user details service to authenticate users with the specified roles.
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // Create an in-memory user with encoded password and role
        UserDetails user = User.builder()
                .username("user")  // Regular user username
                .password(passwordEncoder.encode("password"))  // Encoded password
                .roles("USER")  // Role assigned to user
                .build();

        // Create an in-memory admin with encoded password and role
        UserDetails admin = User.builder()
                .username("admin")  // Admin username
                .password(passwordEncoder.encode("adminpassword"))  // Encoded password
                .roles("ADMIN")  // Admin role
                .build();

        // Return an in-memory user details manager
        return new InMemoryUserDetailsManager(user, admin);  // Return both user and admin details
    }

    /**
     * CORS configuration bean that sets allowed origins, methods, headers, and credentials.
     * This is used to handle cross-origin requests from the frontend.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:3000"));  // Adjust to your frontend URL
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));  // Allow these HTTP methods
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));  // Allow these headers
        corsConfig.setAllowCredentials(true);  // Allow credentials (cookies, etc.)

        // Apply the CORS configuration to all requests
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);  // Apply to all paths
        return source;
    }
}
