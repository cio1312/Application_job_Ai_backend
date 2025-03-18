package com.job.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Jobs job;

    @Column(nullable = false)
    private LocalDateTime appliedAt;

    @Column(nullable = true)
    private String applicationStatus; // Example values: "Pending", "Accepted", "Rejected"

    // Fields to store important user data at the time of application
    @Column(nullable = false)
    private String userEmail;

	/*
	 * @Column(nullable = false) private String userPhoneNumber;
	 */

    @Column(nullable = true)
    private Double userExperience; // Optional user experience details

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Jobs getJob() {
        return job;
    }

    public void setJob(Jobs job) {
        this.job = job;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

	/*
	 * public String getUserPhoneNumber() { return userPhoneNumber; }
	 * 
	 * public void setUserPhoneNumber(String userPhoneNumber) { this.userPhoneNumber
	 * = userPhoneNumber; }
	 */

    public Double getUserExperience() {
        return userExperience;
    }

    public void setUserExperience(Double userExperience) {
        this.userExperience = userExperience;
    }
}
