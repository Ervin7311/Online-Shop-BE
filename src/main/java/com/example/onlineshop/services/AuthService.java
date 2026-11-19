package com.example.onlineshop.services;

import com.example.onlineshop.dto.AuthResponse;
import com.example.onlineshop.dto.LoginRequest;
import com.example.onlineshop.dto.RegisterRequest;
import com.example.onlineshop.entitetes.User;
import com.example.onlineshop.repositories.UserRepository;
import com.example.onlineshop.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername()) || userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Username or Email already in use");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(User.Role.USER);
        user.setStatus(User.Status.ACTIVE);
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        UserDetails userDetails = new CustomUserDetails(user);

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, user.getUsername(), user.getRole(), user.getEmail(), user.getPhoneNumber(), user.getFullName(), user.getId());
    }
}
