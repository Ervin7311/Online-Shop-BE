package com.example.onlineshop.dto;

public record MonthlyOrdersDTO(
        String month,
        long orderCount
) {}
