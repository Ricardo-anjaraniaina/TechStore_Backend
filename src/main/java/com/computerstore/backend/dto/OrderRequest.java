package com.computerstore.backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {
    private String customerEmail;
    private String customerName;
    private String customerPhone;
    private List<OrderItemRequest> items;
}