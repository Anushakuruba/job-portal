package com.anusha.jobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.anusha.jobportal.entity.SavedJob;

public interface SavedJobRepository
        extends JpaRepository<SavedJob, Long> {

    boolean existsByJobIdAndUserEmail(
            Long jobId,
            String userEmail
    );

    List<SavedJob> findByUserEmail(String userEmail);

    @Transactional
    void deleteByJobIdAndUserEmail(
            Long jobId,
            String userEmail
    );
}