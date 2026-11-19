package com.example.onlineshop.dto;

public record DashboardUserDTO(
        Long id,
        String fullName,
        String email,
        String phoneNumber,
        String role,
        String status
) {}
