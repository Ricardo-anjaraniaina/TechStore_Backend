package com.computerstore.backend.service;

import com.computerstore.backend.dto.OrderRequest;
import com.computerstore.backend.dto.OrderResponse;
import com.computerstore.backend.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse checkout(Long userId, OrderRequest request);
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getOrdersByUser(Long userId);
    OrderResponse getOrderById(Long id);
    OrderResponse updateStatus(Long id, OrderStatus status);
    void deleteOrder(Long id);
}
