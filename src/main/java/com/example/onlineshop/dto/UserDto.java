package com.example.onlineshop.dto;

import com.example.onlineshop.entitetes.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String phoneNumber;

    public UserDto(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
    }
}
