package com.anusha.jobportal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.anusha.jobportal.dto.AdminUserDTO;
import com.anusha.jobportal.dto.UpdateUserRoleRequest;
import com.anusha.jobportal.entity.User;
import com.anusha.jobportal.repository.UserRepository;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserRepository userRepository;

    public AdminUserController(
            UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<AdminUserDTO>> getAllUsers(
            Authentication authentication) {

        List<User> users =
                userRepository.findAll();

        List<AdminUserDTO> result =
                new ArrayList<>();

        for (User user : users) {

            AdminUserDTO dto =
                    new AdminUserDTO(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getRole()
                    );

            result.add(dto);
        }

        return ResponseEntity.ok(result);
    }
    @PutMapping("/{userId}/role")
    public ResponseEntity<?> updateUserRole(
            @PathVariable Long userId,
            @RequestBody UpdateUserRoleRequest request,
            Authentication authentication) {

        String adminEmail = authentication.getName();

        User admin =
                userRepository.findByEmail(adminEmail)
                        .orElse(null);

        if (admin == null) {
            return ResponseEntity.status(403)
                    .body("Admin user not found");
        }

        if (admin.getId().equals(userId)) {
            return ResponseEntity.badRequest()
                    .body("Admin cannot change their own role");
        }

        User user =
                userRepository.findById(userId)
                        .orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        String newRole = request.getRole();

        if (newRole == null ||
                (!newRole.equals("JOB_SEEKER")
                && !newRole.equals("RECRUITER"))) {

            return ResponseEntity.badRequest()
                    .body("Role must be JOB_SEEKER or RECRUITER");
        }

        user.setRole(newRole);

        User updatedUser =
                userRepository.save(user);

        AdminUserDTO response =
                new AdminUserDTO(
                        updatedUser.getId(),
                        updatedUser.getName(),
                        updatedUser.getEmail(),
                        updatedUser.getRole()
                );

        return ResponseEntity.ok(response);
    }
}