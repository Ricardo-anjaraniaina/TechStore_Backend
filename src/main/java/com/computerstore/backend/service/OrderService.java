package com.computerstore.backend.service;

import com.computerstore.backend.dto.OrderRequest;
import com.computerstore.backend.dto.OrderResponse;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    List<OrderResponse> getAllOrders();
    OrderResponse getOrderById(Long id);
    OrderResponse updateOrder(Long id, OrderRequest request);
    void deleteOrder(Long id);
}