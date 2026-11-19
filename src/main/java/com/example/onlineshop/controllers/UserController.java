package com.example.onlineshop.controllers;

import com.example.onlineshop.dto.DashboardUserDTO;
import com.example.onlineshop.dto.UserDto;
import com.example.onlineshop.dto.UserUpdateRequest;
import com.example.onlineshop.entitetes.User;
import com.example.onlineshop.services.DashboardService;
import com.example.onlineshop.services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final DashboardService dashboardService;

    public UserController(
            UserService userService,
            DashboardService dashboardService
    ) {
        this.userService = userService;
        this.dashboardService = dashboardService;
    }

    // GET /api/users
    // Only ADMIN can access this endpoint.

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<DashboardUserDTO> getAllUsers() {
        return dashboardService.getDashboardUsers();
    }

    // GET /api/users/{id}

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable Long id
    ) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // GET /api/users/me

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.getUserByUsername(
                userDetails.getUsername()
        );

        return ResponseEntity.ok(user);
    }

    // PUT /api/users/{id}

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest updateRequest
    ) {
        User updatedUser = userService.updateUser(
                id,
                updateRequest
        );

        return ResponseEntity.ok(new UserDto(updatedUser));
    }

    // DELETE /api/users/{id}

    @DeleteMapping("/{id}")
    public void deleteItem(
            @PathVariable Long id
    ) {
        userService.deleteUserById(id);
    }

    // GET /api/users/search

    @GetMapping("/search")
    public List<User> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) User.Role role,
            @RequestParam(required = false) User.Status status
    ) {
        return userService.searchUsers(name, role, status);
    }
}
