package com.computerstore.backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.computerstore.backend.dto.OrderItemResponse;
import com.computerstore.backend.dto.OrderRequest;
import com.computerstore.backend.dto.OrderResponse;
import com.computerstore.backend.entity.Address;
import com.computerstore.backend.entity.CartItem;
import com.computerstore.backend.entity.Order;
import com.computerstore.backend.entity.OrderItem;
import com.computerstore.backend.entity.OrderStatus;
import com.computerstore.backend.entity.User;
import com.computerstore.backend.exception.ResourceNotFoundException;
import com.computerstore.backend.repository.AddressRepository;
import com.computerstore.backend.repository.CartItemRepository;
import com.computerstore.backend.repository.OrderRepository;
import com.computerstore.backend.repository.ProductRepository;
import com.computerstore.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

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
    public OrderResponse checkout(Long userId, OrderRequest request) {
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
        order.setShippingAddress(formatAddress(shipping));
        order.setBillingAddress(formatAddress(billing));
        order.setStatus(OrderStatus.PENDING);

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            var product = cartItem.getProduct();

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
            total = total.add(cartItem.getSubtotal());
        }

        order.setTotalAmount(total);
        Order saved = orderRepository.save(order);

        cartItemRepository.deleteByUserId(userId);

        return toResponse(saved);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public List<OrderResponse> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        return toResponse(findOrder(id));
    }

    @Override
    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatus status) {
        Order order = findOrder(id);
        order.setStatus(status);
        return toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Commande non trouvée");
        }
        orderRepository.deleteById(id);
    }

    private Order findOrder(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Commande non trouvée"));
    }

    private String formatAddress(Address a) {
        return a.getFirstName() + " " + a.getLastName() + ", " +
               a.getStreet() + ", " + a.getCity() + " " + a.getZipCode() + ", " +
               a.getState() + ", " + a.getCountry();
    }

    private OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setUserEmail(order.getUser().getEmail());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setShippingAddress(order.getShippingAddress());
        response.setBillingAddress(order.getBillingAddress());
        response.setCreatedAt(order.getCreatedAt());
        response.setItems(order.getItems().stream().map(this::toItemResponse).toList());
        return response;
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.setId(item.getId());
        response.setProductName(item.getProduct().getName());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setTotalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        return response;
    }
}
