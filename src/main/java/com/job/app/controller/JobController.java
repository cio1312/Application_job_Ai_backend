package com.job.app.controller;

import com.job.app.model.JobApplication;
import com.job.app.model.Jobs;
import com.job.app.model.User;
import com.job.app.service.JobApplicationService;
import com.job.app.service.JobService;
import com.job.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:3000")  // Allow requests from frontend
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private JobApplicationService jobApplicationService;

    /**
     * Save admin dashboard data as a new job entry.
     */
    @PostMapping("/admin-dashboard")
    public ResponseEntity<?> saveAdminDashboardData(@RequestBody Map<String, Object> requestData) {
        System.out.println("=== Admin Dashboard Data Received ===");
        System.out.println("Request Data: " + requestData);

        try {
            // Retrieve adminId from the request data (adminId should be passed by the frontend)
            Long adminId = Long.parseLong(requestData.get("adminId").toString());
            User admin = userService.findById(adminId);

            if (admin == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Admin not found"));
            }

            // Create a new Job entity and set the received data
            Jobs job = new Jobs();
            job.setCompanyName(requestData.get("companyName").toString());
            job.setCompanyTheme(requestData.get("companyTheme").toString());
            job.setCompanyDescription(requestData.get("companyDescription").toString());
            job.setCompanyPolicies(requestData.get("companyPolicies").toString());
            job.setAbout(requestData.get("about").toString());
            job.setContact(requestData.get("contact").toString());
            job.setJobTitle(requestData.get("jobTitle").toString());
            job.setJobDescription(requestData.get("jobDescription").toString());
            job.setLocation(requestData.get("location").toString());
            job.setActive(Boolean.parseBoolean(requestData.get("isActive").toString()));

            // Set the admin (the user creating this job)
            job.setAdmin(admin);

            // Save the job
            Jobs savedJob = jobService.saveJob(job);
            System.out.println("Job saved successfully. ID: " + savedJob.getId());

            // Return the response with the saved job's ID
            return ResponseEntity.ok(Map.of("message", "Profile saved successfully.", "jobId", savedJob.getId()));
        } catch (Exception e) {
            System.out.println("Error while saving profile: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to save profile."));
        }
    }

    /**
     * Fetch jobs created by a specific admin
     */
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<?> getJobsByAdmin(@PathVariable Long adminId) {
        try {
            // Fetch the jobs created by the admin
            List<Jobs> jobs = jobService.getJobsByAdmin(adminId);

            if (jobs.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("message", "No jobs found for this admin"));
            }

            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            System.out.println("Error while fetching jobs: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("message", "Failed to fetch jobs"));
        }
    }

    /**
     * Update the status of a job (Activate/Deactivate)
     */
    @PutMapping("/{jobId}/status")
    public ResponseEntity<?> updateJobStatus(@PathVariable Long jobId, @RequestBody Map<String, Object> requestData) {
        try {
            // Log the incoming request data for debugging purposes
            System.out.println("Received request to update job status for Job ID: " + jobId);
            System.out.println("Request Data: " + requestData +""+"Job ID =  "+jobId);
            
            // Ensure the "isActive" field is present and not null
            if (!requestData.containsKey("isActive") || requestData.get("isActive") == null) {
                return ResponseEntity.status(400).body(Map.of("message", "'isActive' field is required"));
            }

            boolean isActive = Boolean.parseBoolean(requestData.get("isActive").toString());
            
            // Log the parsed 'isActive' value to verify it's being parsed correctly
            System.out.println("Parsed isActive value: " + isActive+ ""+"Job ID "+jobId);

            Jobs job = jobService.updateJobStatus(jobId, isActive);

            return ResponseEntity.ok(Map.of("message", "Job updated successfully", "job", job));
        } catch (Exception e) {
            // Log the error stack trace for better debugging
            e.printStackTrace();
            
            // Log more details about the error
            System.out.println("Error while updating job status: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("message", "Failed to update job status"));
        }
    }

    
    /**
     * Fetch all active jobs
     */
    @GetMapping
    public ResponseEntity<?> getAllJobs() {
        try {
            List<Jobs> jobs = jobService.getAllJobs();
            if (jobs.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("message", "No active jobs found"));
            }
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            System.out.println("Error while fetching active jobs: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("message", "Failed to fetch jobs"));
        }
    }
    
    
    @PostMapping("/apply")
    public ResponseEntity<?> applyForJob(@RequestBody Map<String, Object> requestData) {
        try {
            System.out.println("=== Applying for Job ===");
            System.out.println("Request Data: " + requestData);

            // Extracting userId and jobId from request
            Long userId = Long.parseLong(requestData.get("userId").toString());
            Long jobId = Long.parseLong(requestData.get("jobId").toString());
            System.out.println("Parsed userId: " + userId + ", jobId: " + jobId);

            // Fetch user and job from the database
            User user = userService.findById(userId);
            System.out.println("Fetched User: " + (user != null ? user : "User not found"));

            Jobs job = jobService.findById(jobId);
            System.out.println("Fetched Job: " + (job != null ? job : "Job not found"));

            // Validate user and job existence
            if (user == null || job == null) {
                System.out.println("Invalid user or job ID.");
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid user or job ID"));
            }

            // Create and populate JobApplication entity
            JobApplication application = new JobApplication();
            application.setUser(user);
            application.setJob(job);
            application.setAppliedAt(LocalDateTime.now());
            application.setApplicationStatus("Pending");
            application.setUserEmail(user.getEmail());
			/* application.setUserPhoneNumber(user.getPhoneNumber()); */
            application.setUserExperience(user.getTotalExperience());

            System.out.println("Job Application Details:");
            System.out.println("User: " + user);
            System.out.println("Job: " + job);
            System.out.println("Application: " + application);

            // Save application
            jobApplicationService.saveApplication(application);
            System.out.println("Application saved successfully.");

            return ResponseEntity.ok(Map.of("message", "Application submitted successfully."));
        } catch (Exception e) {
            System.out.println("Error occurred during job application: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Failed to apply for job."));
        }
    }

    /**
     * Fetch job applications for jobs created by a specific admin
     */
    @GetMapping("/admin/{adminId}/applications")
    public ResponseEntity<?> getApplicationsByAdmin(@PathVariable Long adminId) {
        try {
            System.out.println("=== Fetching Applications for Admin ===");
            System.out.println("Admin ID: " + adminId);

            // Fetch jobs created by the admin
            System.out.println("Fetching jobs created by Admin ID: " + adminId);
            List<Jobs> jobs = jobService.getJobsByAdmin(adminId);

            if (jobs.isEmpty()) {
                System.out.println("No jobs found for Admin ID: " + adminId);
                return ResponseEntity.status(404).body(Map.of("message", "No jobs found for this admin"));
            }

            System.out.println("Number of jobs found: " + jobs.size());
            jobs.forEach(job -> System.out.println("Job ID: " + job.getId() + ", Job Title: " + job.getJobTitle()));

            // Collect all applications for jobs created by this admin
            System.out.println("Fetching applications for the above jobs...");
            List<JobApplication> applications = jobs.stream()
                .flatMap(job -> jobApplicationService.getApplicationsByJob(job.getId()).stream())
                .toList();

            if (applications.isEmpty()) {
                System.out.println("No applications found for jobs created by Admin ID: " + adminId);
                return ResponseEntity.status(404).body(Map.of("message", "No applications found for this admin's jobs"));
            }

            System.out.println("Number of applications found: " + applications.size());
            applications.forEach(application -> {
                System.out.println("Application ID: " + application.getId());
                System.out.println("  Job Title: " + application.getJob().getJobTitle());
                System.out.println("  Applicant Name: " + application.getUser().getUsername());
                System.out.println("  Application Status: " + application.getApplicationStatus());
            });

            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            System.out.println("Error while fetching applications for Admin ID: " + adminId);
            System.out.println("Error Message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Failed to fetch applications"));
        }
    }

    
    @GetMapping("/check-payment/{userId}")
    public ResponseEntity<?> checkUserPayment(@PathVariable Long userId) {
        try {
            User user = userService.findById(userId);
            if (user == null) {
                return ResponseEntity.status(404).body(Map.of("message", "User not found"));
            }

            // Check if user has paid for the course
            boolean hasPaid = user.getHasPaidForCourse();
            if (hasPaid) {
                return ResponseEntity.ok(Map.of("message", "Payment successful", "hasPaid", true));
            } else {
                return ResponseEntity.ok(Map.of("message", "Payment not completed", "hasPaid", false));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Error while checking payment"));
        }
    }
    
    
    
    /**
     * ✅ Confirm payment & update the user's status in the database
     */
    @PostMapping("/confirm-payment/{userId}")
    public ResponseEntity<?> confirmUserPayment(@PathVariable Long userId, @RequestBody Map<String, Object> requestData) {
        try {
            System.out.println("Processing payment confirmation for User ID: " + userId);

            User user = userService.findById(userId);
            if (user == null) {
                return ResponseEntity.status(404).body(Map.of("message", "User not found"));
            }

            // Validate that paymentId and orderId exist
            if (!requestData.containsKey("paymentId") || requestData.get("paymentId") == null ||
                !requestData.containsKey("orderId") || requestData.get("orderId") == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Payment ID and Order ID are required"));
            }

            String paymentId = requestData.get("paymentId").toString();
            String orderId = requestData.get("orderId").toString();

            System.out.println("Payment Confirmed for User ID: " + userId);
            System.out.println("Payment ID: " + paymentId);
            System.out.println("Order ID: " + orderId);

            // Update user's payment status
            user.setHasPaidForCourse(true);
            userService.saveUser(user);

            return ResponseEntity.ok(Map.of(
                "message", "Payment confirmed successfully",
                "hasPaid", true,
                "paymentId", paymentId,
                "orderId", orderId
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("message", "Error while confirming payment"));
        }
    }



    
}
