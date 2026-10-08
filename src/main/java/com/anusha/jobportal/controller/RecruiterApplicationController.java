package com.anusha.jobportal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.anusha.jobportal.dto.RecruiterApplicationDTO;
import com.anusha.jobportal.entity.Application;
import com.anusha.jobportal.entity.Job;
import com.anusha.jobportal.repository.ApplicationRepository;
import com.anusha.jobportal.repository.JobRepository;

@RestController
@RequestMapping("/api/recruiter")
public class RecruiterApplicationController {

    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    public RecruiterApplicationController(
            JobRepository jobRepository,
            ApplicationRepository applicationRepository) {

        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }

    @GetMapping("/jobs/{jobId}/applications")
    public ResponseEntity<?> getJobApplications(
            @PathVariable Long jobId,
            Authentication authentication) {

        String recruiterEmail = authentication.getName();

        // Find the job
        Job job = jobRepository
                .findById(jobId)
                .orElse(null);

        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        // Check whether this recruiter owns the job
        if (!job.getRecruiterEmail().equals(recruiterEmail)) {
            return ResponseEntity.status(403)
                    .body("You are not authorized to view applications for this job");
        }

        // Get applications
        List<Application> applications =
                applicationRepository.findByJobId(jobId);

        List<RecruiterApplicationDTO> result =
                new ArrayList<>();

        for (Application application : applications) {

            RecruiterApplicationDTO dto =
                    new RecruiterApplicationDTO(
                            application.getId(),
                            application.getApplicantName(),
                            application.getApplicantEmail(),
                            application.getStatus(),
                            application.getAppliedDate()
                    );

            result.add(dto);
        }

        return ResponseEntity.ok(result);
    }
}