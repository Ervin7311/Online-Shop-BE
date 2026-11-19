package com.example.onlineshop.dto;

import com.example.onlineshop.entitetes.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AuthResponse {
    private String token;
    private String username;
    private User.Role roles;
    private String email;
    private String phoneNumber;
    private String fullName;
    private Long id;
}

