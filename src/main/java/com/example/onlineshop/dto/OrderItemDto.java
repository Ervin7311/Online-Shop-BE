package com.example.onlineshop.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {
    private Long productId;
    private Integer quantity;
    private String productName;
    private double priceAtOrder;
    private String productDescription;
    private String productImageUrl;
}

