package com.example.onlineshop.controllers;

import com.example.onlineshop.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/test")
    public String sendTestEmail(@RequestParam String to) {
        try {
            emailService.sendOrderConfirmation(null, to, "Test User");
        } catch (Exception e) {
            return "Failed to send email: " + e.getMessage();
        }
        return "Email sent successfully!";
    }
}