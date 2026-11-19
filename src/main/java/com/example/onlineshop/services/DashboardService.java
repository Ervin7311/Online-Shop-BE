package com.example.onlineshop.services;

import com.example.onlineshop.dto.*;
import com.example.onlineshop.repositories.DashboardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    private static final DateTimeFormatter MONTH_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM");

    public DashboardService(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    // DATE VALIDATION

    private LocalDateTime[] validateDates(
            String startDate,
            String endDate
    ) {
        LocalDate start;
        LocalDate end;

        try {
            start = LocalDate.parse(
                    startDate,
                    DateTimeFormatter.ISO_LOCAL_DATE
            );

            end = LocalDate.parse(
                    endDate,
                    DateTimeFormatter.ISO_LOCAL_DATE
            );

        } catch (Exception exception) {
            throw new IllegalArgumentException(
                    "Invalid date format. Use YYYY-MM-DD."
            );
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException(
                    "startDate cannot be after endDate."
            );
        }

        return new LocalDateTime[]{
                start.atStartOfDay(),
                end.plusDays(1).atStartOfDay()
        };
    }

    // SUMMARY

    public DashboardSummaryDTO getSummary(
            String startDate,
            String endDate
    ) {
        LocalDateTime[] dates =
                validateDates(startDate, endDate);

        long totalProducts =
                dashboardRepository.countProducts();

        long totalUsers =
                dashboardRepository.countUsers();

        long totalOrders =
                dashboardRepository.countOrders(
                        dates[0], dates[1]
                );

        BigDecimal totalRevenue =
                dashboardRepository.getTotalRevenue(
                        dates[0], dates[1]
                );

        return new DashboardSummaryDTO(
                totalProducts,
                totalUsers,
                totalOrders,
                totalRevenue != null
                        ? totalRevenue
                        : BigDecimal.ZERO
        );
    }

    // SALES

    public List<MonthlySalesDTO> getSales(
            String startDate,
            String endDate
    ) {
        LocalDateTime[] dates =
                validateDates(startDate, endDate);

        return dashboardRepository.getMonthlySales(
                        dates[0], dates[1]
                ).stream()
                .map(row -> new MonthlySalesDTO(
                        formatMonth(row[0], row[1]),
                        row[2] == null
                                ? BigDecimal.ZERO
                                : new BigDecimal(row[2].toString())
                ))
                .toList();
    }

    // MONTHLY ORDERS

    public List<MonthlyOrdersDTO> getOrders(
            String startDate,
            String endDate
    ) {
        LocalDateTime[] dates =
                validateDates(startDate, endDate);

        return dashboardRepository.getMonthlyOrders(
                        dates[0], dates[1]
                ).stream()
                .map(row -> new MonthlyOrdersDTO(
                        formatMonth(row[0], row[1]),
                        ((Number) row[2]).longValue()
                ))
                .toList();
    }

    // NEW USERS

    public List<MonthlyUsersDTO> getUsers(
            String startDate,
            String endDate
    ) {
        LocalDateTime[] dates =
                validateDates(startDate, endDate);

        return dashboardRepository.getMonthlyUsers(
                        dates[0], dates[1]
                ).stream()
                .map(row -> new MonthlyUsersDTO(
                        formatMonth(row[0], row[1]),
                        ((Number) row[2]).longValue()
                ))
                .toList();
    }

    // CURRENT STOCK
    // startDate and endDate are validated but intentionally
    // do not filter stock because stock is a current-state metric.

    public List<StockDistributionDTO> getStockDistribution(
            String startDate,
            String endDate
    ) {
        validateDates(startDate, endDate);

        Map<String, Long> stockMap =
                dashboardRepository.getStockDistribution()
                        .stream()
                        .collect(Collectors.toMap(
                                StockDistributionDTO::status,
                                StockDistributionDTO::count
                        ));

        List<StockDistributionDTO> result =
                new ArrayList<>();

        result.add(new StockDistributionDTO(
                "IN STOCK",
                stockMap.getOrDefault("IN STOCK", 0L)
        ));

        result.add(new StockDistributionDTO(
                "OUT OF STOCK",
                stockMap.getOrDefault("OUT OF STOCK", 0L)
        ));

        return result;
    }

    // TOP FAVORITES
    // Favorites represent the current accumulated number
    // of favorite records, not a date-filtered metric.

    public List<TopFavoriteDTO> getTopFavorites(
            String startDate,
            String endDate
    ) {
        validateDates(startDate, endDate);

        return dashboardRepository.getTopFavoriteProducts();
    }

    // USERS BY ROLE
    // Current user distribution, not filtered by date.

    public List<UserRoleDTO> getUsersByRole(
            String startDate,
            String endDate
    ) {
        validateDates(startDate, endDate);

        Map<String, Long> roleMap =
                dashboardRepository.getUsersByRole()
                        .stream()
                        .collect(Collectors.toMap(
                                UserRoleDTO::role,
                                UserRoleDTO::count
                        ));

        return List.of(
                new UserRoleDTO(
                        "USER",
                        roleMap.getOrDefault("USER", 0L)
                ),
                new UserRoleDTO(
                        "ADMIN",
                        roleMap.getOrDefault("ADMIN", 0L)
                )
        );
    }

    // USER LIST

    public List<DashboardUserDTO> getDashboardUsers() {
        return dashboardRepository.getDashboardUsers();
    }

    private String formatMonth(Object year, Object month) {
        int y = ((Number) year).intValue();
        int m = ((Number) month).intValue();

        return String.format("%04d-%02d", y, m);
    }
}
