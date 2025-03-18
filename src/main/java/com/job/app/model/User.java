package com.job.app.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String email;

	/*
	 * @Column(nullable = false) private String phoneNumber;
	 */

    @Column
    private LocalDate dob;

    @Column
    private Double totalExperience;

    @Column
    private String experienceDetail;

    @Column
    private String currentAddress;

    @Column
    private String resetToken; // Field to store the reset token

    @Column
    private LocalDateTime tokenExpiry; // Optional: expiry time for the token
    
    
    @Column(nullable = false)
    private Boolean hasPaidForCourse = false; // Assuming false by default

    // Enum for user roles
    public enum Role {
        USER,
        ADMIN
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	/*
	 * public String getPhoneNumber() { return phoneNumber; }
	 * 
	 * public void setPhoneNumber(String phoneNumber) { this.phoneNumber =
	 * phoneNumber; }
	 */
    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Double getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(Double totalExperience) {
        this.totalExperience = totalExperience;
    }

    public String getExperienceDetail() {
        return experienceDetail;
    }

    public void setExperienceDetail(String experienceDetail) {
        this.experienceDetail = experienceDetail;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public LocalDateTime getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(LocalDateTime tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }
    


    // Getter and Setter for hasPaidForCourse
    public Boolean getHasPaidForCourse() {
        return hasPaidForCourse;
    }

    public void setHasPaidForCourse(Boolean hasPaidForCourse) {
        this.hasPaidForCourse = hasPaidForCourse;
    }
}
