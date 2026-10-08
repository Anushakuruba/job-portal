package com.anusha.jobportal.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.anusha.jobportal.entity.User;
import com.anusha.jobportal.repository.UserRepository;

@RestController
@RequestMapping("/api/dev")
public class DevPasswordResetController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DevPasswordResetController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/reset-recruiter-password")
    public String resetRecruiterPassword() {

        User user = userRepository
                .findByEmail("recruiter@test.com")
                .orElse(null);

        if (user == null) {
            return "Recruiter account not found";
        }

        user.setPassword(
                passwordEncoder.encode("Recruiter@123")
        );

        userRepository.save(user);

        return "Recruiter password reset successfully";
    }
}