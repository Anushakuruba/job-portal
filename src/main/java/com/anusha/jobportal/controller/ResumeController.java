package com.anusha.jobportal.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.core.Authentication;

import com.anusha.jobportal.entity.JobSeekerProfile;
import com.anusha.jobportal.repository.JobSeekerProfileRepository;

@RestController
@RequestMapping("/api/profile")
public class ResumeController {

    private final JobSeekerProfileRepository profileRepository;

    private final Path uploadDirectory =
            Paths.get("uploads/resumes");

    public ResumeController(
            JobSeekerProfileRepository profileRepository) {

        this.profileRepository = profileRepository;
    }

    // ==========================================
    // UPLOAD RESUME
    // ==========================================

    @PostMapping(
            value = "/resume",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadResume(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        try {

            // Check empty file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Please select a resume file");
            }

            // Check PDF
            String contentType = file.getContentType();

            if (!MediaType.APPLICATION_PDF_VALUE
                    .equalsIgnoreCase(contentType)) {

                return ResponseEntity.badRequest()
                        .body("Only PDF files are allowed");
            }

            // Check file size - maximum 5 MB
            if (file.getSize() > 5 * 1024 * 1024) {

                return ResponseEntity.badRequest()
                        .body("Resume file size must be less than 5 MB");
            }

            String email = authentication.getName();

            // Find profile
            JobSeekerProfile profile =
                    profileRepository.findByEmail(email)
                            .orElse(null);

            if (profile == null) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Please create your profile first");
            }

            // Create upload directory
            Files.createDirectories(uploadDirectory);

            // Generate unique filename
            String fileName =
                    UUID.randomUUID() + ".pdf";

            Path filePath =
                    uploadDirectory.resolve(fileName)
                            .normalize();

            // Save file
            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // Delete previous resume if it exists
            if (profile.getResumeFilePath() != null) {

                try {

                    Path oldFile =
                            Paths.get(profile.getResumeFilePath());

                    Files.deleteIfExists(oldFile);

                } catch (Exception e) {
                	 System.err.println("Could not delete old resume: " + e.getMessage());
                }
            }

            // Save file information
            profile.setResumeFileName(
                    file.getOriginalFilename()
            );

            profile.setResumeFilePath(
                    filePath.toString()
            );

            profileRepository.save(profile);

            return ResponseEntity.ok(
                    "Resume uploaded successfully"
            );

        } catch (IOException e) {

            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body("Failed to upload resume");
        }
    }
    @GetMapping("/resume")
    public ResponseEntity<?> downloadResume(
            Authentication authentication) {

        try {

            String email = authentication.getName();

            JobSeekerProfile profile =
                    profileRepository.findByEmail(email)
                            .orElse(null);

            if (profile == null) {
                return ResponseEntity.notFound().build();
            }

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

            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body("Failed to read resume");
        }
    }
}