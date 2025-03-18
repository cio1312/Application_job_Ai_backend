package com.job.app.service;

import com.job.app.model.JobApplication;
import com.job.app.repository.JobApplicationRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    public JobApplication saveApplication(JobApplication application) {
        return jobApplicationRepository.save(application);
    }
    
    /**
     * Retrieve all applications for a specific job.
     * 
     * @param jobId The ID of the job.
     * @return List of job applications.
     */
    public List<JobApplication> getApplicationsByJob(Long jobId) {
        return jobApplicationRepository.findByJobId(jobId);
    }
}
