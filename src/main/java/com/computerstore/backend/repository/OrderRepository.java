package com.computerstore.backend.repository;

import com.computerstore.backend.entity.Order;
import com.computerstore.backend.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Order> findByStatus(OrderStatus status);
    Optional<Order> findByOrderNumber(String orderNumber);
    Optional<Order> findByIdAndUserId(Long id, Long userId);
}
