package com.job.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "jobs")
public class Jobs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private String jobDescription;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private boolean isActive = true;  // Default to active

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)  // Represents the User (admin) who created the job
    private User admin;  // Link to the user (admin) who created the job

    // Admin dashboard fields
    private String companyName;
    private String companyTheme;
    private String companyDescription;
    private String companyPolicies;
    private String about;
    private String contact;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    // Getters and Setters for the new fields (admin dashboard)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyTheme() {
        return companyTheme;
    }

    public void setCompanyTheme(String companyTheme) {
        this.companyTheme = companyTheme;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getCompanyPolicies() {
        return companyPolicies;
    }

    public void setCompanyPolicies(String companyPolicies) {
        this.companyPolicies = companyPolicies;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
