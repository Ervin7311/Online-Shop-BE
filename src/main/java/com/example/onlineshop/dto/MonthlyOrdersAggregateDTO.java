package com.example.onlineshop.dto;

public record MonthlyOrdersAggregateDTO(
        Integer year,
        Integer month,
        Long orderCount
) {}
