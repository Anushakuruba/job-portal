package com.anusha.jobportal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.anusha.jobportal.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

    // Search by title
    List<Job> findByTitleContainingIgnoreCase(String title);

    // Filter by location
    List<Job> findByLocationContainingIgnoreCase(String location);

    // Filter by employment type
    List<Job> findByEmploymentTypeIgnoreCase(String employmentType);

    // Search by title + location
    List<Job> findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(
            String title,
            String location
    );

    // Pagination
    Page<Job> findByTitleContainingIgnoreCase(
            String title,
            Pageable pageable
    );

    Page<Job> findByLocationContainingIgnoreCase(
            String location,
            Pageable pageable
    );

    // Recruiter's jobs
    List<Job> findByRecruiterEmail(String recruiterEmail);

    // ⭐ New: skills filter
    List<Job> findBySkillsContainingIgnoreCase(String skills);

    // ⭐ New: salary filter
    List<Job> findBySalaryGreaterThanEqual(Double salary);

    // ⭐ New: title + location + employment type
    List<Job> findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCaseAndEmploymentTypeIgnoreCase(
            String title,
            String location,
            String employmentType
    );
}