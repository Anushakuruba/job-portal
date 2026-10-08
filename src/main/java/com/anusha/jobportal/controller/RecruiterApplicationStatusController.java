package com.anusha.jobportal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.anusha.jobportal.dto.UpdateApplicationStatusRequest;
import com.anusha.jobportal.entity.Application;
import com.anusha.jobportal.entity.Job;
import com.anusha.jobportal.repository.ApplicationRepository;
import com.anusha.jobportal.repository.JobRepository;

@RestController
@RequestMapping("/api/recruiter/applications")
public class RecruiterApplicationStatusController {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    public RecruiterApplicationStatusController(
            ApplicationRepository applicationRepository,
            JobRepository jobRepository) {

        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
    }

    @PutMapping("/{applicationId}/status")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestBody UpdateApplicationStatusRequest request,
            Authentication authentication) {

        String recruiterEmail = authentication.getName();

        // Find application
        Application application =
                applicationRepository
                        .findById(applicationId)
                        .orElse(null);

        if (application == null) {
            return ResponseEntity.notFound().build();
        }

        // Find the job associated with the application
        Job job =
                jobRepository
                        .findById(application.getJobId())
                        .orElse(null);

        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        // Check recruiter ownership
        if (!job.getRecruiterEmail().equals(recruiterEmail)) {
            return ResponseEntity.status(403)
                    .body("You are not authorized to update this application");
        }

        // Validate status
        if (request.getStatus() == null) {
            return ResponseEntity.badRequest()
                    .body("Application status is required");
        }

        // Update status
        application.setStatus(request.getStatus());

        Application updatedApplication =
                applicationRepository.save(application);

        return ResponseEntity.ok(updatedApplication);
    }
}