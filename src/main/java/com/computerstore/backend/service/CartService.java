package com.computerstore.backend.service;

import com.computerstore.backend.dto.CartItemRequest;
import com.computerstore.backend.dto.CartItemResponse;
import com.computerstore.backend.dto.CartResponse;
import com.computerstore.backend.entity.CartItem;
import com.computerstore.backend.entity.Product;
import com.computerstore.backend.entity.User;
import com.computerstore.backend.exception.ResourceNotFoundException;
import com.computerstore.backend.repository.CartItemRepository;
import com.computerstore.backend.repository.ProductRepository;
import com.computerstore.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Ajouter un article au panier
     */
    public CartItemResponse addToCart(Long userId, CartItemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Vérifier le stock
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStockQuantity());
        }

        // Vérifier si l'article est déjà dans le panier
        CartItem existingItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId())
                .orElse(null);

        if (existingItem != null) {
            // Augmenter la quantité
            int newQuantity = existingItem.getQuantity() + request.getQuantity();
            if (product.getStockQuantity() < newQuantity) {
                throw new RuntimeException("Insufficient stock. Available: " + product.getStockQuantity());
            }
            existingItem.setQuantity(newQuantity);
            CartItem updated = cartItemRepository.save(existingItem);
            return convertToResponse(updated);
        } else {
            // Créer un nouveau CartItem
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice());

            CartItem saved = cartItemRepository.save(cartItem);
            return convertToResponse(saved);
        }
    }

    /**
     * Mettre à jour la quantité d'un article
     */
    public CartItemResponse updateCartItem(Long userId, Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        // Vérifier que le panier appartient à l'utilisateur
        if (!cartItem.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        // Vérifier le stock
        if (cartItem.getProduct().getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + cartItem.getProduct().getStockQuantity());
        }

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        cartItem.setQuantity(quantity);
        CartItem updated = cartItemRepository.save(cartItem);
        return convertToResponse(updated);
    }

    /**
     * Supprimer un article du panier
     */
    public void removeFromCart(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cartItem.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        cartItemRepository.deleteById(cartItemId);
    }

    /**
     * Obtenir le panier complet de l'utilisateur
     */
    public CartResponse getCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        List<CartItemResponse> items = cartItems.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CartResponse response = new CartResponse();
        response.setUserId(userId);
        response.setItems(items);
        response.setTotalItems(cartItems.size());
        response.setTotalPrice(totalPrice);

        return response;
    }

    /**
     * Vider le panier
     */
    public void clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        cartItemRepository.deleteByUserId(userId);
    }

    /**
     * Vérifier si un produit est dans le panier
     */
    public boolean isProductInCart(Long userId, Long productId) {
        return cartItemRepository.findByUserIdAndProductId(userId, productId).isPresent();
    }

    /**
     * Obtenir le nombre d'articles dans le panier
     */
    public Integer getCartItemCount(Long userId) {
        return cartItemRepository.countByUserId(userId);
    }

    // Méthode utilitaire de conversion
    private CartItemResponse convertToResponse(CartItem cartItem) {
        CartItemResponse response = new CartItemResponse();
        response.setId(cartItem.getId());
        response.setProductId(cartItem.getProduct().getId());
        response.setProductName(cartItem.getProduct().getName());
        response.setProductImage(cartItem.getProduct().getImageUrl());
        response.setQuantity(cartItem.getQuantity());
        response.setPrice(cartItem.getPrice());
        response.setSubtotal(cartItem.getSubtotal());
        return response;
    }
}
