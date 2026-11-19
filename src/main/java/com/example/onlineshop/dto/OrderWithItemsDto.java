package com.example.onlineshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderWithItemsDto {
        private Long orderId;
        private LocalDateTime orderDate;
        private String status;
        private BigDecimal totalAmount;
        private List<OrderItemDto> items;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class OrderItemDto {
                private String productName;
                private int quantity;
                private double price;
        }
}
