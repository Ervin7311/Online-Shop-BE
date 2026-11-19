package com.example.onlineshop.repositories;

import com.example.onlineshop.dto.DashboardUserDTO;
import com.example.onlineshop.dto.StockDistributionDTO;
import com.example.onlineshop.dto.TopFavoriteDTO;
import com.example.onlineshop.dto.UserRoleDTO;
import com.example.onlineshop.entitetes.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Product, Long> {

    // CURRENT TOTALS

    @Query("SELECT COUNT(p) FROM Product p")
    long countProducts();

    @Query("SELECT COUNT(u) FROM User u")
    long countUsers();

    // ORDERS IN SELECTED PERIOD

    @Query("""
        SELECT COUNT(o)
        FROM Order o
        WHERE o.orderDate >= :startDate
        AND o.orderDate < :endDate
    """)
    long countOrders(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // REVENUE IN SELECTED PERIOD
    // Uses Order.totalAmount, not Payment.amountPaid.
    // This follows the existing order-level total.

    @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0)
        FROM Order o
        WHERE o.orderDate >= :startDate
        AND o.orderDate < :endDate
    """)
    BigDecimal getTotalRevenue(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // MONTHLY SALES


// MONTHLY SALES

    @Query("""
    SELECT
        FUNCTION('YEAR', o.orderDate),
        FUNCTION('MONTH', o.orderDate),
        SUM(o.totalAmount)
    FROM Order o
    WHERE o.orderDate >= :startDate
      AND o.orderDate < :endDate
    GROUP BY
        FUNCTION('YEAR', o.orderDate),
        FUNCTION('MONTH', o.orderDate)
    ORDER BY
        FUNCTION('YEAR', o.orderDate),
        FUNCTION('MONTH', o.orderDate)
""")
    List<Object[]> getMonthlySales(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


// MONTHLY ORDERS

    @Query("""
    SELECT
        FUNCTION('YEAR', o.orderDate),
        FUNCTION('MONTH', o.orderDate),
        COUNT(o)
    FROM Order o
    WHERE o.orderDate >= :startDate
      AND o.orderDate < :endDate
    GROUP BY
        FUNCTION('YEAR', o.orderDate),
        FUNCTION('MONTH', o.orderDate)
    ORDER BY
        FUNCTION('YEAR', o.orderDate),
        FUNCTION('MONTH', o.orderDate)
""")
    List<Object[]> getMonthlyOrders(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


// MONTHLY NEW USERS

    @Query("""
    SELECT
        FUNCTION('YEAR', u.createdAt),
        FUNCTION('MONTH', u.createdAt),
        COUNT(u)
    FROM User u
    WHERE u.createdAt >= :startDate
      AND u.createdAt < :endDate
    GROUP BY
        FUNCTION('YEAR', u.createdAt),
        FUNCTION('MONTH', u.createdAt)
    ORDER BY
        FUNCTION('YEAR', u.createdAt),
        FUNCTION('MONTH', u.createdAt)
""")
    List<Object[]> getMonthlyUsers(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    // CURRENT PRODUCT STOCK

    @Query("""
        SELECT new com.example.onlineshop.dto.StockDistributionDTO(
            CASE
                WHEN p.stockQuantity <= 0 THEN 'OUT OF STOCK'
                ELSE 'IN STOCK'
            END,
            COUNT(p)
        )
        FROM Product p
        GROUP BY
            CASE
                WHEN p.stockQuantity <= 0 THEN 'OUT OF STOCK'
                ELSE 'IN STOCK'
            END
    """)
    List<StockDistributionDTO> getStockDistribution();

    // TOP FAVORITE PRODUCTS

    @Query("""
        SELECT new com.example.onlineshop.dto.TopFavoriteDTO(
            f.product.name,
            COUNT(f)
        )
        FROM Favorite f
        GROUP BY f.product.Id, f.product.name
        ORDER BY COUNT(f) DESC
    """)
    List<TopFavoriteDTO> getTopFavoriteProducts();

    // USERS BY ROLE
    // Only USER and ADMIN are returned.
    // Existing MANAGER records are left untouched.

    @Query("""
        SELECT new com.example.onlineshop.dto.UserRoleDTO(
            CAST(u.role AS string),
            COUNT(u)
        )
        FROM User u
        WHERE u.role IN (
            com.example.onlineshop.entitetes.User.Role.USER,
            com.example.onlineshop.entitetes.User.Role.ADMIN
        )
        GROUP BY u.role
    """)
    List<UserRoleDTO> getUsersByRole();

    // DASHBOARD USER LIST

    @Query("""
        SELECT new com.example.onlineshop.dto.DashboardUserDTO(
            u.id,
            u.fullName,
            u.email,
            u.phoneNumber,
            CAST(u.role AS string),
            CAST(u.status AS string)
        )
        FROM User u
        WHERE u.role IN (
            com.example.onlineshop.entitetes.User.Role.USER,
            com.example.onlineshop.entitetes.User.Role.ADMIN
        )
        ORDER BY u.id DESC
    """)
    List<DashboardUserDTO> getDashboardUsers();
}
