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

public class UserUpdateRequest {

        private String fullName;
        private String username;
        private String email;
        private String phoneNumber;
        private User.Status status;
        private User.Role role;
}
