package com.anusha.jobportal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.anusha.jobportal.entity.JobSeekerProfile;
import com.anusha.jobportal.repository.JobSeekerProfileRepository;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final JobSeekerProfileRepository profileRepository;

    public ProfileController(
            JobSeekerProfileRepository profileRepository) {

        this.profileRepository = profileRepository;
    }

    // ==========================================
    // CREATE PROFILE
    // ==========================================

    @PostMapping
    public ResponseEntity<?> createProfile(
            @RequestBody JobSeekerProfile profile,
            Authentication authentication) {

        String email = authentication.getName();

        // Prevent duplicate profile
        if (profileRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest()
                    .body("Profile already exists");
        }

        // Never trust email from request body
        profile.setEmail(email);

        JobSeekerProfile savedProfile =
                profileRepository.save(profile);

        return ResponseEntity.ok(savedProfile);
    }

    // ==========================================
    // GET MY PROFILE
    // ==========================================

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(
            Authentication authentication) {

        String email = authentication.getName();

        return profileRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    // ==========================================
    // UPDATE MY PROFILE
    // ==========================================

    @PutMapping("/me")
    public ResponseEntity<?> updateMyProfile(
            @RequestBody JobSeekerProfile profile,
            Authentication authentication) {

        String email = authentication.getName();

        JobSeekerProfile existingProfile =
                profileRepository.findByEmail(email)
                        .orElse(null);

        if (existingProfile == null) {
            return ResponseEntity.notFound().build();
        }

        existingProfile.setPhone(profile.getPhone());
        existingProfile.setLocation(profile.getLocation());
        existingProfile.setSkills(profile.getSkills());
        existingProfile.setEducation(profile.getEducation());
        existingProfile.setExperience(profile.getExperience());
        existingProfile.setGithubUrl(profile.getGithubUrl());
        existingProfile.setLinkedinUrl(profile.getLinkedinUrl());

        JobSeekerProfile updatedProfile =
                profileRepository.save(existingProfile);

        return ResponseEntity.ok(updatedProfile);
    }
}