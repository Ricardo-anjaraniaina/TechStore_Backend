package com.computerstore.backend.service;

import com.computerstore.backend.dto.AddressResponse;
import com.computerstore.backend.dto.CreateOrderRequest;
import com.computerstore.backend.dto.OrderItemResponse;
import com.computerstore.backend.dto.OrderResponse;
import com.computerstore.backend.dto.UpdateOrderStatusRequest;
import com.computerstore.backend.entity.*;
import com.computerstore.backend.exception.ResourceNotFoundException;
import com.computerstore.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Le panier est vide");
        }

        Address shipping = addressRepository.findByIdAndUserId(request.getShippingAddressId(), userId)
            .orElseThrow(() -> new ResourceNotFoundException("Adresse de livraison non trouvée"));

        Address billing = addressRepository.findByIdAndUserId(request.getBillingAddressId(), userId)
            .orElseThrow(() -> new ResourceNotFoundException("Adresse de facturation non trouvée"));

        Order order = new Order();
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setUser(user);
        order.setShippingAddress(shipping);
        order.setBillingAddress(billing);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setNotes(request.getNotes());

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new IllegalStateException("Stock insuffisant pour: " + product.getName());
            }

            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(cartItem.getQuantity());
            item.setUnitPrice(cartItem.getPrice());
            order.getItems().add(item);
        }

        order.calculateTotal();
        Order saved = orderRepository.save(order);
        cartItemRepository.deleteByUserId(userId);

        return toResponse(saved);
    }

    @Override
    public List<OrderResponse> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
            .stream().map(this::toResponse).toList();
    }

    @Override
    public OrderResponse getOrderById(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée"));
        return toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long userId, Long orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée"));

        OrderStatus newStatus = OrderStatus.valueOf(request.getStatus().toUpperCase());
        order.setStatus(newStatus);

        if (request.getTrackingNumber() != null) {
            order.setTrackingNumber(request.getTrackingNumber());
        }
        if (newStatus == OrderStatus.SHIPPED) {
            order.setShippedAt(LocalDateTime.now());
        }
        if (newStatus == OrderStatus.DELIVERED) {
            order.setDeliveredAt(LocalDateTime.now());
        }

        return toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée"));

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Impossible d'annuler une commande déjà expédiée ou livrée");
        }

        // Remettre le stock
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);
        return toResponse(orderRepository.save(order));
    }

    @Override
    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status)
            .stream().map(this::toResponse).toList();
    }

    private OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setStatus(order.getStatus().name());
        response.setSubtotal(order.getSubtotal());
        response.setShippingFee(order.getShippingFee());
        response.setTaxAmount(order.getTaxAmount());
        response.setTotalAmount(order.getTotalAmount());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setTrackingNumber(order.getTrackingNumber());
        response.setNotes(order.getNotes());
        response.setCreatedAt(order.getCreatedAt());
        response.setShippedAt(order.getShippedAt());
        response.setDeliveredAt(order.getDeliveredAt());
        response.setShippingAddress(toAddressResponse(order.getShippingAddress()));
        response.setBillingAddress(toAddressResponse(order.getBillingAddress()));
        response.setItems(order.getItems().stream().map(this::toItemResponse).toList());
        return response;
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.setId(item.getId());
        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getName());
        response.setProductImage(item.getProduct().getImageUrl());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setSubtotal(item.getUnitPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())));
        return response;
    }

    private AddressResponse toAddressResponse(Address address) {
        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setFirstName(address.getFirstName());
        response.setLastName(address.getLastName());
        response.setStreet(address.getStreet());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setZipCode(address.getZipCode());
        response.setCountry(address.getCountry());
        response.setPhoneNumber(address.getPhoneNumber());
        response.setIsDefault(address.getIsDefault());
        response.setAddressType(address.getAddressType());
        response.setInstructions(address.getInstructions());
        return response;
    }
}
