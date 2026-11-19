package com.example.onlineshop.services;

import com.example.onlineshop.dto.AllOrdersResponsDto;
import com.example.onlineshop.dto.OrderItemDto;
import com.example.onlineshop.dto.OrderWithItemsDto;
import com.example.onlineshop.entitetes.*;
import com.example.onlineshop.repositories.CardRepository;
import com.example.onlineshop.repositories.OrderItemRepository;
import com.example.onlineshop.repositories.OrderRepository;
import com.example.onlineshop.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final CardRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }


    public List<OrderWithItemsDto> getOrdersByUser(User user) {
        List<Order> orders = orderRepository.findByUserIdWithItems(user.getId());
        return orders.stream()
                .map(this::convertToOrderWithItemsDto)
                .collect(Collectors.toList());
    }

    private OrderWithItemsDto convertToOrderWithItemsDto(Order order) {
        OrderWithItemsDto dto = new OrderWithItemsDto();
        dto.setOrderId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());

        List<OrderWithItemsDto.OrderItemDto> items = order.getItems().stream().map(item -> {
            OrderWithItemsDto.OrderItemDto itemDto = new OrderWithItemsDto.OrderItemDto();
            itemDto.setProductName(item.getProduct().getName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPriceAtOrder() * item.getQuantity());
            return itemDto;
        }).toList();

        dto.setItems(items);

        double totalAmountDouble = items.stream()
                .mapToDouble(OrderWithItemsDto.OrderItemDto::getPrice)
                .sum();

        dto.setTotalAmount(BigDecimal.valueOf(totalAmountDouble));

        return dto;
    }

    public Order createOrderFromCart(Long userId, String shippingAddress, String paymentMethod) {

        List<Card> cartItems = cartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Card card : cartItems) {
            Product product = productRepository.findById(card.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            BigDecimal price = product.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(card.getQuantity());
            totalAmount = totalAmount.add(price.multiply(quantity));
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        order = orderRepository.save(order);

        for (Card card : cartItems) {

            Product product = productRepository.findById(card.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            int currentStock = product.getStockQuantity();
            int orderedQty = card.getQuantity();

            if (orderedQty > currentStock) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            product.setStockQuantity(currentStock - orderedQty);
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(card.getQuantity());
            item.setPriceAtOrder(product.getPrice().doubleValue());

            orderItemRepository.save(item);
        }

        cartRepository.deleteByUserId(userId);

        return order;
    }

    public List<AllOrdersResponsDto> getAllOrdersAsDto() {

        return orderRepository.findAll().stream().map(order -> {

            AllOrdersResponsDto dto = new AllOrdersResponsDto();
            dto.setId(order.getId());
            dto.setUserId(order.getUserId());
            dto.setOrderDate(order.getOrderDate().toString());
            dto.setStatus(order.getStatus());
            dto.setShippingAddress(order.getShippingAddress());
            dto.setPaymentMethod(order.getPaymentMethod());

            List<OrderItemDto> itemDtos = order.getItems().stream().map(item ->
                    OrderItemDto.builder()
                            .productId(item.getProduct().getId())
                            .productName(item.getProduct().getName())
                            .quantity(item.getQuantity())
                            .priceAtOrder(item.getPriceAtOrder())
                            .productDescription(item.getProduct().getDescription())
                            .productImageUrl(item.getProduct().getImageUrl())
                            .build()
            ).toList();

            dto.setItems(itemDtos);

            double totalAmount = itemDtos.stream()
                    .mapToDouble(i -> i.getPriceAtOrder() * i.getQuantity())
                    .sum();

            dto.setTotalAmount(BigDecimal.valueOf(totalAmount));

            return dto;

        }).toList();
    }

    public List<AllOrdersResponsDto> getOrders(int year, Integer month) {

        List<Order> orders;

        if (month == null) {
            orders = orderRepository.findOrdersByYear(year);
        } else {
            orders = orderRepository.findOrdersByYearAndMonth(year, month);
        }

        return orders.stream().map(this::convertToDto).toList();
    }

    private AllOrdersResponsDto convertToDto(Order order) {
        return new AllOrdersResponsDto(
                order.getId(),
                order.getUserId(),
                order.getOrderDate().toString(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getShippingAddress(),
                order.getPaymentMethod(),
                order.getItems().stream()
                        .map(item -> OrderItemDto.builder()
                                .productName(item.getProduct().getName())
                                .quantity(item.getQuantity())
                                .priceAtOrder(item.getPriceAtOrder())
                                .productDescription(item.getProduct().getDescription())
                                .build()
                        )
                        .toList()
        );
    }
}
