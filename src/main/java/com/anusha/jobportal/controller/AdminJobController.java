package com.anusha.jobportal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.anusha.jobportal.dto.AdminJobDTO;
import com.anusha.jobportal.entity.Job;
import com.anusha.jobportal.repository.ApplicationRepository;
import com.anusha.jobportal.repository.JobRepository;

@RestController
@RequestMapping("/api/admin/jobs")
public class AdminJobController {

    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    public AdminJobController(
            JobRepository jobRepository,
            ApplicationRepository applicationRepository) {

        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }

    @GetMapping
    public ResponseEntity<List<AdminJobDTO>> getAllJobs(
            Authentication authentication) {

        List<Job> jobs =
                jobRepository.findAll();

        List<AdminJobDTO> result =
                new ArrayList<>();

        for (Job job : jobs) {

            AdminJobDTO dto =
                    new AdminJobDTO(
                            job.getId(),
                            job.getTitle(),
                            job.getCompanyName(),
                            job.getLocation(),
                            job.getEmploymentType(),
                            job.getSalary(),
                            job.getExperienceRequired(),
                            job.getSkills(),
                            job.getRecruiterEmail(),
                            job.getPostedDate()
                    );

            result.add(dto);
        }

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<?> deleteJob(
            @PathVariable Long jobId,
            Authentication authentication) {

        Job job =
                jobRepository.findById(jobId)
                        .orElse(null);

        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        // Delete applications first
        applicationRepository.deleteByJobId(jobId);

        // Delete the job
        jobRepository.delete(job);

        return ResponseEntity.ok(
                "Job and associated applications deleted successfully"
        );
    }
}