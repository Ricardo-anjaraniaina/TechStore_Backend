package com.computerstore.backend.controller;

import com.computerstore.backend.dto.CreateOrderRequest;
import com.computerstore.backend.dto.OrderResponse;
import com.computerstore.backend.dto.UpdateOrderStatusRequest;
import com.computerstore.backend.entity.OrderStatus;
import com.computerstore.backend.security.UserPrincipal;
import com.computerstore.backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * Créer une commande
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            Long userId = getCurrentUserId();
            OrderResponse response = orderService.createOrder(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            log.error("Error creating order", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create order");
        }
    }

    /**
     * Récupérer toutes les commandes de l'utilisateur
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrders() {
        Long userId = getCurrentUserId();
        List<OrderResponse> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Récupérer une commande spécifique
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
        try {
            Long userId = getCurrentUserId();
            OrderResponse order = orderService.getOrderById(userId, orderId);
            return ResponseEntity.ok(order);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /**
     * Mettre à jour le statut d'une commande
     */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        try {
            Long userId = getCurrentUserId();
            OrderResponse response = orderService.updateOrderStatus(userId, orderId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            log.error("Error updating order status", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update order status");
        }
    }

    /**
     * Annuler une commande
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        try {
            Long userId = getCurrentUserId();
            OrderResponse response = orderService.cancelOrder(userId, orderId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            log.error("Error canceling order", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to cancel order");
        }
    }

    /**
     * Récupérer les commandes par statut (Admin)
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable String status) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            List<OrderResponse> orders = orderService.getOrdersByStatus(orderStatus);
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Obtenir l'ID de l'utilisateur connecté
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new RuntimeException("User not authenticated");
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getId();
    }
}
