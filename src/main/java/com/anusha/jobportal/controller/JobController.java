package com.anusha.jobportal.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anusha.jobportal.entity.Job;
import com.anusha.jobportal.repository.JobRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobRepository jobRepository;

    public JobController(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    // =========================================================
    // CREATE JOB
    // =========================================================

    @PostMapping
    public ResponseEntity<?> createJob(
            @Valid @RequestBody Job job,
            Authentication authentication) {

        job.setRecruiterEmail(authentication.getName());
        job.setPostedDate(LocalDateTime.now());

        Job savedJob = jobRepository.save(job);

        return ResponseEntity.ok(savedJob);
    }

    // =========================================================
    // GET ALL JOBS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {

        return ResponseEntity.ok(
                jobRepository.findAll()
        );
    }

    // =========================================================
    // GET JOB BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(
            @PathVariable Long id) {

        return jobRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    // =========================================================
    // ADVANCED SEARCH + FILTER
    // =========================================================

    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobs(

            @RequestParam(required = false)
            String keyword,

            @RequestParam(required = false)
            String location,

            @RequestParam(required = false)
            String employmentType,

            @RequestParam(required = false)
            Double minSalary,

            @RequestParam(defaultValue = "postedDate")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String direction) {

        /*
         * Start with all jobs.
         * Then apply each filter only when the user provides it.
         */

        List<Job> jobs = jobRepository.findAll();

        // =====================================================
        // KEYWORD FILTER
        // Searches both TITLE and SKILLS
        // =====================================================

        if (keyword != null && !keyword.trim().isEmpty()) {

            String searchKeyword =
                    keyword.trim().toLowerCase();

            jobs = jobs.stream()
                    .filter(job ->

                            (job.getTitle() != null
                                    && job.getTitle()
                                    .toLowerCase()
                                    .contains(searchKeyword))

                            ||

                            (job.getSkills() != null
                                    && job.getSkills()
                                    .toLowerCase()
                                    .contains(searchKeyword))
                    )
                    .collect(Collectors.toList());
        }

        // =====================================================
        // LOCATION FILTER
        // =====================================================

        if (location != null && !location.trim().isEmpty()) {

            String searchLocation =
                    location.trim().toLowerCase();

            jobs = jobs.stream()
                    .filter(job ->
                            job.getLocation() != null
                                    && job.getLocation()
                                    .toLowerCase()
                                    .contains(searchLocation)
                    )
                    .collect(Collectors.toList());
        }

        // =====================================================
        // EMPLOYMENT TYPE FILTER
        // =====================================================

        if (employmentType != null
                && !employmentType.trim().isEmpty()) {

            String type =
                    employmentType.trim().toLowerCase();

            jobs = jobs.stream()
                    .filter(job ->
                            job.getEmploymentType() != null
                                    && job.getEmploymentType()
                                    .toLowerCase()
                                    .equals(type)
                    )
                    .collect(Collectors.toList());
        }

        // =====================================================
        // MINIMUM SALARY FILTER
        // =====================================================

        if (minSalary != null) {

            jobs = jobs.stream()
                    .filter(job ->
                            job.getSalary() != null
                                    && job.getSalary() >= minSalary
                    )
                    .collect(Collectors.toList());
        }

        // =====================================================
        // SORTING
        // =====================================================

        if (sortBy.equalsIgnoreCase("salary")) {

            if (direction.equalsIgnoreCase("asc")) {

                jobs.sort(
                        (job1, job2) ->
                                Double.compare(
                                        job1.getSalary() == null
                                                ? 0
                                                : job1.getSalary(),

                                        job2.getSalary() == null
                                                ? 0
                                                : job2.getSalary()
                                )
                );

            } else {

                jobs.sort(
                        (job1, job2) ->
                                Double.compare(
                                        job2.getSalary() == null
                                                ? 0
                                                : job2.getSalary(),

                                        job1.getSalary() == null
                                                ? 0
                                                : job1.getSalary()
                                )
                );
            }

        } else {

            // Default sorting = posted date

            if (direction.equalsIgnoreCase("asc")) {

                jobs.sort(
                        (job1, job2) ->
                                compareDates(
                                        job1.getPostedDate(),
                                        job2.getPostedDate()
                                )
                );

            } else {

                jobs.sort(
                        (job1, job2) ->
                                compareDates(
                                        job2.getPostedDate(),
                                        job1.getPostedDate()
                                )
                );
            }
        }

        return ResponseEntity.ok(jobs);
    }

    // =========================================================
    // DATE COMPARISON HELPER
    // =========================================================

    private int compareDates(
            LocalDateTime date1,
            LocalDateTime date2) {

        if (date1 == null && date2 == null) {
            return 0;
        }

        if (date1 == null) {
            return -1;
        }

        if (date2 == null) {
            return 1;
        }

        return date1.compareTo(date2);
    }

    // =========================================================
    // PAGINATION + SORTING
    // =========================================================

    @GetMapping("/page")
    public ResponseEntity<Page<Job>> getJobsWithPagination(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size,

            @RequestParam(defaultValue = "postedDate")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String direction) {

        Sort sort;

        if (direction.equalsIgnoreCase("asc")) {

            sort = Sort.by(sortBy).ascending();

        } else {

            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );

        Page<Job> jobs =
                jobRepository.findAll(pageable);

        return ResponseEntity.ok(jobs);
    }

    // =========================================================
    // UPDATE JOB
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(

            @PathVariable Long id,

            @Valid @RequestBody Job updatedJob,

            Authentication authentication) {

        String recruiterEmail =
                authentication.getName();

        Job existingJob =
                jobRepository
                        .findById(id)
                        .orElse(null);

        if (existingJob == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        // Check whether recruiter owns the job

        if (!existingJob
                .getRecruiterEmail()
                .equals(recruiterEmail)) {

            return ResponseEntity
                    .status(403)
                    .body(
                            "You are not authorized to update this job"
                    );
        }

        // Update job details

        existingJob.setTitle(
                updatedJob.getTitle()
        );

        existingJob.setDescription(
                updatedJob.getDescription()
        );

        existingJob.setCompanyName(
                updatedJob.getCompanyName()
        );

        existingJob.setLocation(
                updatedJob.getLocation()
        );

        existingJob.setEmploymentType(
                updatedJob.getEmploymentType()
        );

        existingJob.setSalary(
                updatedJob.getSalary()
        );

        existingJob.setExperienceRequired(
                updatedJob.getExperienceRequired()
        );

        existingJob.setSkills(
                updatedJob.getSkills()
        );

        // Keep recruiter email

        existingJob.setRecruiterEmail(
                recruiterEmail
        );

        Job savedJob =
                jobRepository.save(existingJob);

        return ResponseEntity.ok(savedJob);
    }

    // =========================================================
    // DELETE JOB
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(

            @PathVariable Long id,

            Authentication authentication) {

        String recruiterEmail =
                authentication.getName();

        Job existingJob =
                jobRepository
                        .findById(id)
                        .orElse(null);

        if (existingJob == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        // Check whether recruiter owns the job

        if (!existingJob
                .getRecruiterEmail()
                .equals(recruiterEmail)) {

            return ResponseEntity
                    .status(403)
                    .body(
                            "You are not authorized to delete this job"
                    );
        }

        jobRepository.delete(existingJob);

        return ResponseEntity.ok(
                "Job deleted successfully"
        );
    }
}