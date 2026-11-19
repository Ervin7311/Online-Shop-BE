package com.example.onlineshop.dto;

import java.math.BigDecimal;

public record MonthlySalesAggregateDTO(
        Integer year,
        Integer month,
        BigDecimal revenue
) {}
