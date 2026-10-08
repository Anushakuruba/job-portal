package com.anusha.jobportal.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.anusha.jobportal.entity.Application;
import com.anusha.jobportal.entity.ApplicationStatus;
import com.anusha.jobportal.entity.Job;
import com.anusha.jobportal.entity.JobSeekerProfile;
import com.anusha.jobportal.entity.User;
import com.anusha.jobportal.repository.ApplicationRepository;
import com.anusha.jobportal.repository.JobRepository;
import com.anusha.jobportal.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import jakarta.validation.Valid;

import org.springframework.http.MediaType;

import com.anusha.jobportal.repository.JobSeekerProfileRepository;

import java.util.ArrayList;
import com.anusha.jobportal.dto.ApplicationDashboardDTO;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobSeekerProfileRepository profileRepository;
    public ApplicationController(
            ApplicationRepository applicationRepository,
            UserRepository userRepository,
            JobRepository jobRepository,
            JobSeekerProfileRepository profileRepository) {

        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.profileRepository = profileRepository;
    }
    
    // ==========================================
    // JOB SEEKER - APPLY FOR JOB
    // ==========================================

    @PostMapping
    public ResponseEntity<?> applyForJob(
    		 @Valid @RequestBody Application application,
            Authentication authentication) {

        String applicantEmail = authentication.getName();

        // Check whether job exists
        Job job = jobRepository
                .findById(application.getJobId())
                .orElse(null);

        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        // Prevent duplicate application
        if (applicationRepository
                .existsByJobIdAndApplicantEmail(
                        application.getJobId(),
                        applicantEmail)) {

            return ResponseEntity.badRequest()
                    .body("You have already applied for this job");
        }

        // Find applicant
        User user = userRepository
                .findByEmail(applicantEmail)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest()
                    .body("User not found");
        }

        application.setApplicantEmail(applicantEmail);
        application.setApplicantName(user.getName());
        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedDate(LocalDateTime.now());

        Application savedApplication =
                applicationRepository.save(application);

        return ResponseEntity.ok(savedApplication);
    }

    // ==========================================
    // JOB SEEKER - VIEW MY APPLICATIONS
    // ==========================================

    @GetMapping("/my")
    public ResponseEntity<List<Application>> getMyApplications(
            Authentication authentication) {

        String applicantEmail = authentication.getName();

        return ResponseEntity.ok(
                applicationRepository
                        .findByApplicantEmail(applicantEmail)
        );
    }

    // ==========================================
    // RECRUITER - VIEW APPLICATIONS FOR OWN JOB
    // ==========================================

    @GetMapping("/job/{jobId}")
    public ResponseEntity<?> getApplicationsForJob(
            @PathVariable Long jobId,
            Authentication authentication) {

        String recruiterEmail = authentication.getName();

        // Find job
        Job job = jobRepository
                .findById(jobId)
                .orElse(null);

        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        // Check job ownership
        if (!job.getRecruiterEmail().equals(recruiterEmail)) {
            return ResponseEntity.status(403)
                    .body("You are not authorized to view applications for this job");
        }

        return ResponseEntity.ok(
                applicationRepository.findByJobId(jobId)
        );
    }

    // ==========================================
    // RECRUITER - UPDATE APPLICATION STATUS
    // ==========================================

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status,
            Authentication authentication) {

        String recruiterEmail = authentication.getName();

        // Find application
        Application application = applicationRepository
                .findById(id)
                .orElse(null);

        if (application == null) {
            return ResponseEntity.notFound().build();
        }

        // Find related job
        Job job = jobRepository
                .findById(application.getJobId())
                .orElse(null);

        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        // Check job ownership
        if (!job.getRecruiterEmail().equals(recruiterEmail)) {
            return ResponseEntity.status(403)
                    .body("You are not authorized to update this application");
        }

        application.setStatus(status);

        Application updatedApplication =
                applicationRepository.save(application);

        return ResponseEntity.ok(updatedApplication);
    }
    
    @GetMapping("/{applicationId}/resume")
    public ResponseEntity<?> getApplicantResume(
            @PathVariable Long applicationId,
            Authentication authentication) {

        try {

            String recruiterEmail = authentication.getName();

            // Find application
            Application application =
                    applicationRepository.findById(applicationId)
                            .orElse(null);

            if (application == null) {
                return ResponseEntity.notFound().build();
            }

            // Find related job
            Job job =
                    jobRepository.findById(application.getJobId())
                            .orElse(null);

            if (job == null) {
                return ResponseEntity.notFound().build();
            }

            // Check recruiter owns the job
            if (!job.getRecruiterEmail().equals(recruiterEmail)) {
                return ResponseEntity.status(403)
                        .body("You are not authorized to access this resume");
            }

            // Find applicant profile
            JobSeekerProfile profile =
                    profileRepository
                            .findByEmail(application.getApplicantEmail())
                            .orElse(null);

            if (profile == null) {
                return ResponseEntity.notFound().build();
            }

            // Check resume exists
            if (profile.getResumeFilePath() == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath =
                    Paths.get(profile.getResumeFilePath());

            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            byte[] fileBytes =
                    Files.readAllBytes(filePath);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(
                            "Content-Disposition",
                            "inline; filename=\""
                                    + profile.getResumeFileName()
                                    + "\""
                    )
                    .body(fileBytes);

        } catch (IOException e) {

            return ResponseEntity.internalServerError()
                    .body("Failed to read resume");
        }
    }
    @GetMapping("/dashboard")
    public ResponseEntity<List<ApplicationDashboardDTO>> getApplicationDashboard(
            Authentication authentication) {

        String applicantEmail = authentication.getName();

        List<Application> applications =
                applicationRepository.findByApplicantEmail(applicantEmail);

        List<ApplicationDashboardDTO> dashboard =
                new ArrayList<>();

        for (Application application : applications) {

            Job job = jobRepository
                    .findById(application.getJobId())
                    .orElse(null);

            // Skip application if the job no longer exists
            if (job == null) {
                continue;
            }

            ApplicationDashboardDTO dto =
                    new ApplicationDashboardDTO(
                            application.getId(),
                            job.getId(),
                            job.getTitle(),
                            job.getCompanyName(),
                            job.getLocation(),
                            job.getEmploymentType(),
                            application.getStatus(),
                            application.getAppliedDate()
                    );

            dashboard.add(dto);
        }

        return ResponseEntity.ok(dashboard);
    }
}