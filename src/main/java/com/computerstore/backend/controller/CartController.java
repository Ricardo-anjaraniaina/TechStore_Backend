package com.computerstore.backend.controller;

import com.computerstore.backend.dto.CartItemRequest;
import com.computerstore.backend.dto.CartItemResponse;
import com.computerstore.backend.dto.CartResponse;
import com.computerstore.backend.security.UserPrincipal;
import com.computerstore.backend.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * Obtenir le panier de l'utilisateur connecté
     */
    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        Long userId = getCurrentUserId();
        CartResponse cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Ajouter un article au panier
     */
    @PostMapping("/items")
    public ResponseEntity<?> addToCart(@RequestBody CartItemRequest request) {
        try {
            Long userId = getCurrentUserId();
            CartItemResponse response = cartService.addToCart(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            log.error("Error adding to cart", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add item to cart");
        }
    }

    /**
     * Mettre à jour la quantité d'un article dans le panier
     */
    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        try {
            Long userId = getCurrentUserId();
            CartItemResponse response = cartService.updateCartItem(userId, cartItemId, quantity);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            log.error("Error updating cart item", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update cart item");
        }
    }

    /**
     * Supprimer un article du panier
     */
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long cartItemId) {
        try {
            Long userId = getCurrentUserId();
            cartService.removeFromCart(userId, cartItemId);
            return ResponseEntity.ok().body("Item removed from cart");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            log.error("Error removing from cart", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove item from cart");
        }
    }

    /**
     * Vider le panier
     */
    @DeleteMapping
    public ResponseEntity<?> clearCart() {
        try {
            Long userId = getCurrentUserId();
            cartService.clearCart(userId);
            return ResponseEntity.ok().body("Cart cleared");
        } catch (Exception ex) {
            log.error("Error clearing cart", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to clear cart");
        }
    }

    /**
     * Obtenir le nombre d'articles dans le panier
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> getCartItemCount() {
        Long userId = getCurrentUserId();
        Integer count = cartService.getCartItemCount(userId);
        return ResponseEntity.ok(count);
    }

    // Obtenir l'ID de l'utilisateur connecté (sans requête BD!)
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new RuntimeException("User not authenticated");
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return principal.getId();
    }
}
