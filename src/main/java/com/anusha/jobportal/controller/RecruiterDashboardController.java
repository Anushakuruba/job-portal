package com.anusha.jobportal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.anusha.jobportal.dto.RecruiterDashboardDTO;
import com.anusha.jobportal.entity.Job;
import com.anusha.jobportal.repository.ApplicationRepository;
import com.anusha.jobportal.repository.JobRepository;

@RestController
@RequestMapping("/api/recruiter")
public class RecruiterDashboardController {

    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    public RecruiterDashboardController(
            JobRepository jobRepository,
            ApplicationRepository applicationRepository) {

        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<RecruiterDashboardDTO>> getDashboard(
            Authentication authentication) {

        String recruiterEmail = authentication.getName();

        List<Job> jobs =
                jobRepository.findByRecruiterEmail(recruiterEmail);

        List<RecruiterDashboardDTO> dashboard =
                new ArrayList<>();

        for (Job job : jobs) {

            long totalApplications =
                    applicationRepository.countByJobId(
                            job.getId()
                    );

            RecruiterDashboardDTO dto =
                    new RecruiterDashboardDTO(
                            job.getId(),
                            job.getTitle(),
                            job.getCompanyName(),
                            job.getLocation(),
                            job.getEmploymentType(),
                            totalApplications
                    );

            dashboard.add(dto);
        }

        return ResponseEntity.ok(dashboard);
    }
}