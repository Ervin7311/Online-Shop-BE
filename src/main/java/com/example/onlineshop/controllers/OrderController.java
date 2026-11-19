package com.example.onlineshop.controllers;

import com.example.onlineshop.dto.AllOrdersResponsDto;
import com.example.onlineshop.dto.OrderWithItemsDto;
import com.example.onlineshop.entitetes.Order;
import com.example.onlineshop.entitetes.User;
import com.example.onlineshop.security.CustomUserDetails;
import com.example.onlineshop.services.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<Order> checkout(@PathVariable Long userId, @RequestBody OrderRequest orderRequest) {
        Order order = orderService.createOrderFromCart(userId, orderRequest.getShippingAddress(), orderRequest.getPaymentMethod());
        return ResponseEntity.ok(order);
    }

    @GetMapping("/by/user")
    public ResponseEntity<List<OrderWithItemsDto>> getOrdersForUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        List<OrderWithItemsDto> orders = orderService.getOrdersByUser(user);
        return ResponseEntity.ok(orders);
    }

    @GetMapping
    public ResponseEntity<List<AllOrdersResponsDto>> getAllOrders() {
        List<AllOrdersResponsDto> orders = orderService.getAllOrdersAsDto();
        return ResponseEntity.ok(orders);
    }


    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.findOrderById(id);
    }


    public static class OrderRequest {
        private String shippingAddress;
        private String paymentMethod;

        public String getShippingAddress() {
            return shippingAddress;
        }

        public void setShippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }
    }
}
