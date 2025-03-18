package com.job.app.repository;

import com.job.app.model.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Jobs, Long> {

    // Custom query to find jobs by admin ID
    List<Jobs> findByAdminId(Long adminId);
    
    // Fetch only active jobs
    List<Jobs> findByIsActiveTrue();
}
