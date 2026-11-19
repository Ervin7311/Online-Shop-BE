package com.example.onlineshop.dto;

import java.math.BigDecimal;

public record MonthlySalesDTO(
        String month,
        BigDecimal revenue
) {}
