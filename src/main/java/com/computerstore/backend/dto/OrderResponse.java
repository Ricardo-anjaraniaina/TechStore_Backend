package com.computerstore.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.computerstore.backend.entity.OrderStatus;

import lombok.Data;

@Data
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private String customerEmail;
    private String customerName;
    private String customerPhone;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}