package com.example.onlineshop.repositories;


import com.example.onlineshop.entitetes.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items WHERE o.userId = :userId")
    List<Order> findByUserIdWithItems(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE YEAR(o.orderDate) = :year")
    List<Order> findOrdersByYear(@Param("year") int year);

    @Query("SELECT o FROM Order o WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month")
    List<Order> findOrdersByYearAndMonth(@Param("year") int year, @Param("month") int month);
}


