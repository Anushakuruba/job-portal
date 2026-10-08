package com.anusha.jobportal.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.anusha.jobportal.entity.Job;
import com.anusha.jobportal.entity.SavedJob;
import com.anusha.jobportal.repository.JobRepository;
import com.anusha.jobportal.repository.SavedJobRepository;

@RestController
@RequestMapping("/api/saved-jobs")
public class SavedJobController {

    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;

    public SavedJobController(
            SavedJobRepository savedJobRepository,
            JobRepository jobRepository) {

        this.savedJobRepository = savedJobRepository;
        this.jobRepository = jobRepository;
    }

    // ==========================================
    // SAVE JOB
    // ==========================================

    @PostMapping("/{jobId}")
    public ResponseEntity<?> saveJob(
            @PathVariable Long jobId,
            Authentication authentication) {

        String userEmail = authentication.getName();

        // Check job exists
        Job job = jobRepository
                .findById(jobId)
                .orElse(null);

        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        // Prevent duplicate save
        if (savedJobRepository
                .existsByJobIdAndUserEmail(
                        jobId,
                        userEmail)) {

            return ResponseEntity.badRequest()
                    .body("Job already saved");
        }

        SavedJob savedJob = new SavedJob();

        savedJob.setJobId(jobId);
        savedJob.setUserEmail(userEmail);
        savedJob.setSavedDate(LocalDateTime.now());

        SavedJob result =
                savedJobRepository.save(savedJob);

        return ResponseEntity.ok(result);
    }

    // ==========================================
    // GET MY SAVED JOBS
    // ==========================================

    @GetMapping
    public ResponseEntity<List<SavedJob>> getMySavedJobs(
            Authentication authentication) {

        String userEmail = authentication.getName();

        return ResponseEntity.ok(
                savedJobRepository
                        .findByUserEmail(userEmail)
        );
    }

    // ==========================================
    // REMOVE SAVED JOB
    // ==========================================

    @DeleteMapping("/{jobId}")
    public ResponseEntity<?> removeSavedJob(
            @PathVariable Long jobId,
            Authentication authentication) {

        String userEmail = authentication.getName();

        if (!savedJobRepository
                .existsByJobIdAndUserEmail(
                        jobId,
                        userEmail)) {

            return ResponseEntity.notFound().build();
        }

        savedJobRepository.deleteByJobIdAndUserEmail(
                jobId,
                userEmail
        );

        return ResponseEntity.ok(
                "Job removed from saved jobs"
        );
    }
}