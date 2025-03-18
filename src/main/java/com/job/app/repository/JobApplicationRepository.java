package com.job.app.repository;

import com.job.app.model.JobApplication;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
	
    /**
     * Find job applications by job ID.
     *
     * @param jobId The ID of the job.
     * @return List of job applications.
     */
    List<JobApplication> findByJobId(Long jobId);
}
