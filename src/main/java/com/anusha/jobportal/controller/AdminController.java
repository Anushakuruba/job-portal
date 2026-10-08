package com.anusha.jobportal.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anusha.jobportal.dto.AdminApplicationDTO;
import com.anusha.jobportal.dto.AdminApplicationStatsDTO;
import com.anusha.jobportal.entity.Application;
import com.anusha.jobportal.entity.ApplicationStatus;
import com.anusha.jobportal.entity.Job;
import com.anusha.jobportal.entity.User;
import com.anusha.jobportal.repository.ApplicationRepository;
import com.anusha.jobportal.repository.JobRepository;
import com.anusha.jobportal.repository.UserRepository;
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    
    public AdminController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            ApplicationRepository applicationRepository,
            JobRepository jobRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(
            @RequestBody User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest()
                    .body("Email already registered");
        }

        User admin = new User();

        admin.setName(user.getName());
        admin.setEmail(user.getEmail());
        admin.setPassword(
                passwordEncoder.encode(user.getPassword())
        );
        admin.setRole("ADMIN");

        User savedAdmin =
                userRepository.save(admin);

        savedAdmin.setPassword(null);

        return ResponseEntity.ok(savedAdmin);
    }

    @GetMapping("/application-stats")
    public ResponseEntity<AdminApplicationStatsDTO> getApplicationStats() {

        long totalApplications = applicationRepository.count();

        long applied =
                applicationRepository.countByStatus(
                        ApplicationStatus.APPLIED);

        long shortlisted =
                applicationRepository.countByStatus(
                        ApplicationStatus.SHORTLISTED);

        long interview =
                applicationRepository.countByStatus(
                        ApplicationStatus.INTERVIEW);

        long selected =
                applicationRepository.countByStatus(
                        ApplicationStatus.SELECTED);

        long rejected =
                applicationRepository.countByStatus(
                        ApplicationStatus.REJECTED);

        AdminApplicationStatsDTO stats =
                new AdminApplicationStatsDTO(
                        totalApplications,
                        applied,
                        shortlisted,
                        interview,
                        selected,
                        rejected
                );

        return ResponseEntity.ok(stats);
    }
    @GetMapping("/applications")
    public ResponseEntity<?> getAllApplications() {

        List<Application> applications =
                applicationRepository.findAll();

        List<AdminApplicationDTO> result =
                applications.stream()
                        .map(application -> {

                            Job job = jobRepository
                                    .findById(application.getJobId())
                                    .orElse(null);

                            if (job == null) {
                                return new AdminApplicationDTO(
                                        application.getId(),
                                        application.getJobId(),
                                        application.getApplicantName(),
                                        application.getApplicantEmail(),
                                        "Job Not Found",
                                        "N/A",
                                        "N/A",
                                        "N/A",
                                        application.getStatus().name(),
                                        application.getCoverLetter(),
                                        application.getAppliedDate()
                                );
                            }

                            return new AdminApplicationDTO(
                                    application.getId(),
                                    application.getJobId(),
                                    application.getApplicantName(),
                                    application.getApplicantEmail(),
                                    job.getTitle(),
                                    job.getCompanyName(),
                                    job.getLocation(),
                                    job.getEmploymentType(),
                                    application.getStatus().name(),
                                    application.getCoverLetter(),
                                    application.getAppliedDate()
                            );
                        })
                        .toList();

        return ResponseEntity.ok(result);
    }
}