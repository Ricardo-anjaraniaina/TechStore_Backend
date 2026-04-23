package com.computerstore.backend.service;

import com.computerstore.backend.dto.CreateOrderRequest;
import com.computerstore.backend.dto.OrderResponse;
import com.computerstore.backend.dto.UpdateOrderStatusRequest;
import com.computerstore.backend.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(Long userId, CreateOrderRequest request);
    List<OrderResponse> getUserOrders(Long userId);
    OrderResponse getOrderById(Long userId, Long orderId);
    OrderResponse updateOrderStatus(Long userId, Long orderId, UpdateOrderStatusRequest request);
    OrderResponse cancelOrder(Long userId, Long orderId);
    List<OrderResponse> getOrdersByStatus(OrderStatus status);
}
