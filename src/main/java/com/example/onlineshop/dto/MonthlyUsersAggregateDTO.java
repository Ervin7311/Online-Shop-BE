package com.example.onlineshop.dto;

public record MonthlyUsersAggregateDTO(
        Integer year,
        Integer month,
        Long userCount
) {}
