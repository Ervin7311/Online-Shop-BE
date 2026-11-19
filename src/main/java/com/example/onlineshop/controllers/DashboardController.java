package com.example.onlineshop.controllers;

import com.example.onlineshop.dto.*;
import com.example.onlineshop.services.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
//@PreAuthorize("hasRole('ADMIN')")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService
    ) {
        this.dashboardService = dashboardService;
    }

    // GET /dashboard/summary

    @GetMapping("/summary")
    public DashboardSummaryDTO getSummary(
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        return dashboardService.getSummary(
                startDate, endDate
        );
    }

    // GET /dashboard/sales

    @GetMapping("/sales")
    public List<MonthlySalesDTO> getSales(
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        return dashboardService.getSales(
                startDate, endDate
        );
    }

    // GET /dashboard/orders

    @GetMapping("/orders")
    public List<MonthlyOrdersDTO> getOrders(
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        return dashboardService.getOrders(
                startDate, endDate
        );
    }

    // GET /dashboard/users

    @GetMapping("/users")
    public List<MonthlyUsersDTO> getUsers(
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        return dashboardService.getUsers(
                startDate, endDate
        );
    }

    // GET /dashboard/products/stock

    @GetMapping("/products/stock")
    public List<StockDistributionDTO> getStock(
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        return dashboardService.getStockDistribution(
                startDate, endDate
        );
    }

    // GET /dashboard/products/top-favorites

    @GetMapping("/products/top-favorites")
    public List<TopFavoriteDTO> getTopFavorites(
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        return dashboardService.getTopFavorites(
                startDate, endDate
        );
    }

    // GET /dashboard/users/roles

    @GetMapping("/users/roles")
    public List<UserRoleDTO> getUsersByRole(
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        return dashboardService.getUsersByRole(
                startDate, endDate
        );
    }
}
