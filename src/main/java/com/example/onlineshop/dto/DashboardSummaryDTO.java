package com.example.onlineshop.dto;

import java.math.BigDecimal;

public record DashboardSummaryDTO(
        long totalProducts,
        long totalUsers,
        long totalOrders,
        BigDecimal totalRevenue
) {}
