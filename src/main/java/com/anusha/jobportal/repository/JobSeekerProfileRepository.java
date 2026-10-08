package com.anusha.jobportal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anusha.jobportal.entity.JobSeekerProfile;

public interface JobSeekerProfileRepository
        extends JpaRepository<JobSeekerProfile, Long> {

    Optional<JobSeekerProfile> findByEmail(String email);

    boolean existsByEmail(String email);
}