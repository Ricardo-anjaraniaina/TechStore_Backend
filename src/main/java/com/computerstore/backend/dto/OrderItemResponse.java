package com.computerstore.backend.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Long id;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}