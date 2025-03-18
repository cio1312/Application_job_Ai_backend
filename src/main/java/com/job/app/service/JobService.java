package com.job.app.service;

import com.job.app.model.Jobs;
import com.job.app.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    /**
     * Save a new job to the database
     *
     * @param job Job object
     * @return Saved Job object
     */
    public Jobs saveJob(Jobs job) {
        return jobRepository.save(job);
    }

    /**
     * Get all jobs created by a specific admin
     *
     * @param adminId ID of the admin
     * @return List of Jobs created by the admin
     */
    public List<Jobs> getJobsByAdmin(Long adminId) {
        return jobRepository.findByAdminId(adminId); // This method fetches jobs based on adminId
    }

    /**
     * Update the status of a job
     *
     * @param jobId    ID of the job
     * @param isActive New status (true for active, false for inactive)
     * @return Updated Job object
     */
    public Jobs updateJobStatus(Long jobId, boolean isActive) {
        Jobs job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        job.setActive(isActive);
        Jobs updatedJob = jobRepository.save(job);

        // Console output for debugging
        System.out.println("Job ID: " + jobId + " updated successfully. New Status: " + (isActive ? "Active" : "Inactive"));

        return updatedJob;
    }
    
    /**
     * Fetch all jobs
     *
     * @return List of all jobs
     */
    // Fetch only active jobs
    public List<Jobs> getAllJobs() {
        return jobRepository.findByIsActiveTrue();
    }
    
    public Jobs findById(Long jobId) {
        return jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));
    }
    
    

}
