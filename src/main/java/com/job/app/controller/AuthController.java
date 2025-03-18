package com.job.app.controller;

import com.job.app.model.User;
import com.job.app.service.EmailService;
import com.job.app.service.UserService;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    @Autowired
    private EmailService emailService; // Inject EmailService

    // Registration endpoint
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        System.out.println("\n\n** Incoming registration request for username: " + user.getUsername() + " **");

        // Check if the username already exists
        if (userService.findByUsername(user.getUsername()) != null) {
            System.out.println("** Username already exists: " + user.getUsername() + " **");
            return ResponseEntity.badRequest().body("Username already exists");
        }

        if (user.getRole() == null) {
            user.setRole(User.Role.USER);
            System.out.println("** Role not provided. Setting default role to USER **");
        }

        // Ensure email or phone number is provided
        if (user.getEmail() == null) {
            System.out.println("** Email  missing for: " + user.getUsername() + " **");
            return ResponseEntity.badRequest().body("Email is required.");
        }

        // Handle user role as ADMIN
        if (user.getRole() == User.Role.ADMIN) {
            user.setDob(null);
            user.setTotalExperience(null);
            user.setExperienceDetail(null);
            user.setCurrentAddress(null);
            System.out.println("** Admin user detected. Nullifying personal details. **");
        }

        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("** Password encrypted for username: " + user.getUsername() + " **");

        userService.saveUser(user);  // Save user
        System.out.println("** User saved successfully: " + user.getUsername() + " **");

        return ResponseEntity.ok("User registered successfully");
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        System.out.println("\n\n** Incoming login request for username: " + loginRequest.getUsername() + " **");

        // Find user by username
        User user = userService.findByUsername(loginRequest.getUsername());
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            System.out.println("** Invalid credentials for username: " + loginRequest.getUsername() + " **");
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        System.out.println("** Login successful for username: " + loginRequest.getUsername() + " with role: " + user.getRole() + " **");
        return ResponseEntity.ok(new LoginResponse("Login successful", user.getRole().name(), user.getId()));
    }

    // Profile endpoint
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestParam String username) {
        System.out.println("\n\n** Fetching profile for username: " + username + " **");

        // Find user by username
        User user = userService.findByUsername(username);
        if (user == null) {
            System.out.println("** User not found for username: " + username + " **");
            return ResponseEntity.badRequest().body("User not found");
        }

        System.out.println("** Profile fetched for username: " + username + " **");
        return ResponseEntity.ok(user);
    }

    // Forgot password endpoint
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        System.out.println("\n\n** Forgot password request for username: " + passwordResetRequest.getUsername() + " **");

        // Find user by username and email
        User user = userService.findByUsernameAndEmail(passwordResetRequest.getUsername(), passwordResetRequest.getEmail());
        if (user == null) {
            System.out.println("** Invalid user ID or email for username: " + passwordResetRequest.getUsername() + " **");
            return ResponseEntity.badRequest().body("Invalid user ID or email.");
        }

        // Generate reset token and set expiration
        String resetToken = generateResetToken(user);
        user.setResetToken(resetToken);
        user.setTokenExpiry(LocalDateTime.now().plusHours(1));
        userService.saveUser(user);
        System.out.println("** Password reset token generated for username: " + user.getUsername() + " **");

        // Send email with reset instructions
        String resetLink = "http://localhost:3000/reset-password?token=" + resetToken;
        String emailBody = "Hello " + user.getUsername() + ",\n\n"
                + "To reset your password, please click the following link:\n"
                + resetLink + "\n\n"
                + "If you did not request this, please ignore this email.\n\n"
                + "Best Regards,\nLinkify";
        emailService.sendEmail(passwordResetRequest.getEmail(), "Password Reset Request", emailBody);

        System.out.println("** Password reset instructions sent to email: " + passwordResetRequest.getEmail() + " **");
        return ResponseEntity.ok("Password reset instructions have been sent to your email.");
    }

    // Reset password endpoint
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest resetRequest) {
        System.out.println("\n\n** Reset password request with token: " + resetRequest.getToken() + " **");

        // Find user by reset token
        User user = userService.findByResetToken(resetRequest.getToken());
        if (user == null || user.getTokenExpiry() == null || user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            System.out.println("** Invalid or expired reset token for username: " + resetRequest.getUsername() + " **");
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(resetRequest.getNewPassword()));
        user.setResetToken(null);
        user.setTokenExpiry(null);
        userService.saveUser(user);

        System.out.println("** Password successfully reset for username: " + resetRequest.getUsername() + " **");
        return ResponseEntity.ok("Password reset successful. Please log in with your new password.");
    }

    // Helper method to generate a password reset token
    private String generateResetToken(User user) {
        return UUID.randomUUID().toString();
    }

    // DTO for capturing password reset request details
    public static class PasswordResetRequest {
        private String username;
        private String email;
        private String token;
        private String newPassword;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

    // DTO for login response
    public static class LoginResponse {
        private String message;
        private String role;
        private Long adminId;

        public LoginResponse(String message, String role, Long adminId) {
            this.message = message;
            this.role = role;
            this.adminId = adminId;
        }

        public String getMessage() {
            return message;
        }

        public String getRole() {
            return role;
        }

        public Long getAdminId() {
            return adminId;
        }
    }
    
    // Endpoint to handle contact form submission
    @PostMapping("/contact")
    public ResponseEntity<String> sendContactEmail(@RequestBody ContactForm contactForm) {
        try {
            // Send email to the company (swaroopmodel01@gmail.com)
            String companyEmail = "swaroopmodel01@gmail.com";
            String subject = contactForm.getSubject();
            String body = contactForm.getBody();
            String userEmail = contactForm.getEmail();

            // Construct the email body to send to the company
            String companyMessage = "You have received a new message from: " + userEmail + "\n\n" + 
                                    "Subject: " + subject + "\n\n" + "Message: " + body;

            // Send email to company
            emailService.sendEmail(companyEmail, subject, companyMessage);
            System.out.println("Email sent to company (swaroopmodel01@gmail.com) with subject: " + subject);

            // Send a thank you email to the user
            String thankYouSubject = "Thank You for Contacting Us!";
            String thankYouBody = "Dear User,\n\nThank you for reaching out to us. We will get back to you soon.\n\n" +
                                  "Best regards,\nLinkify\nContact Us: support@linkify.com\nWebsite: www.linkify.com";

            // Send thank you email to the user
            emailService.sendEmail(userEmail, thankYouSubject, thankYouBody);
            System.out.println("Thank you email sent to user: " + userEmail);

            return ResponseEntity.ok("Your message has been sent successfully.");
        } catch (Exception e) {
            // Log the error and return a response with status 500
            System.out.println("Error sending email: " + e.getMessage());
            return ResponseEntity.status(500).body("An error occurred while sending your message. Please try again later.");
        }
    }

    // DTO for contact form submission
    public static class ContactForm {
        private String subject;
        private String email;
        private String body;

        // Getters and setters
        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }
    
}
