package com.example.onlineshop.services;

import com.example.onlineshop.dto.UserUpdateRequest;
import com.example.onlineshop.entitetes.User;
import com.example.onlineshop.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserUpdateRequest updateRequest = new UserUpdateRequest();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(Long id, UserUpdateRequest updateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updateRequest.getFullName() != null) {
            user.setFullName(updateRequest.getFullName());
        }
        if (updateRequest.getUsername() != null) {
            user.setUsername(updateRequest.getUsername());
        }
        if (updateRequest.getEmail() != null) {
            user.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(updateRequest.getPhoneNumber());
        }
        if (updateRequest.getStatus() != null) {
            user.setStatus(updateRequest.getStatus());
        }
        if (updateRequest.getRole() != null) {
            user.setRole(updateRequest.getRole());
        }


        return userRepository.save(user);
    }

    public void deleteUserById(Long Id) {
        User userId = userRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Item not in cart"));
        userRepository.delete(userId);
    }

    public List<User> searchUsers(String name, User.Role role, User.Status status) {
        return userRepository.searchUsers(name, role, status);
    }
}

