package com.computerstore.backend.controller;

import com.computerstore.backend.dto.AddressRequest;
import com.computerstore.backend.dto.AddressResponse;
import com.computerstore.backend.security.UserPrincipal;
import com.computerstore.backend.service.AddressService;
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
@RequestMapping("/api/addresses")
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class AddressController {
    @Autowired
    private AddressService addressService;

    /**
     * Créer une nouvelle adresse
     */
    @PostMapping
    public ResponseEntity<?> createAddress(@Valid @RequestBody AddressRequest request) {
        try {
            Long userId = getCurrentUserId();
            AddressResponse response = addressService.createAddress(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            log.error("Error creating address", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create address");
        }
    }

    /**
     * Récupérer toutes les adresses de l'utilisateur
     */
    @GetMapping
    public ResponseEntity<List<AddressResponse>> getUserAddresses() {
        Long userId = getCurrentUserId();
        List<AddressResponse> addresses = addressService.getAddressesByUserId(userId);
        return ResponseEntity.ok(addresses);
    }

    /**
     * Récupérer une adresse spécifique
     */
    @GetMapping("/{addressId}")
    public ResponseEntity<?> getAddress(@PathVariable Long addressId) {
        try {
            Long userId = getCurrentUserId();
            AddressResponse address = addressService.getAddressById(userId, addressId);
            return ResponseEntity.ok(address);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /**
     * Mettre à jour une adresse
     */
    @PutMapping("/{addressId}")
    public ResponseEntity<?> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request) {
        try {
            Long userId = getCurrentUserId();
            AddressResponse response = addressService.updateAddress(userId, addressId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            log.error("Error updating address", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update address");
        }
    }

    /**
     * Supprimer une adresse
     */
    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long addressId) {
        try {
            Long userId = getCurrentUserId();
            addressService.deleteAddress(userId, addressId);
            return ResponseEntity.ok().body("Address deleted successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            log.error("Error deleting address", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete address");
        }
    }

    /**
     * Obtenir l'adresse par défaut
     */
    @GetMapping("/default")
    public ResponseEntity<?> getDefaultAddress() {
        try {
            Long userId = getCurrentUserId();
            AddressResponse address = addressService.getDefaultAddress(userId);
            return ResponseEntity.ok(address);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /**
     * Définir une adresse comme adresse par défaut
     */
    @PutMapping("/{addressId}/set-default")
    public ResponseEntity<?> setDefaultAddress(@PathVariable Long addressId) {
        try {
            Long userId = getCurrentUserId();
            AddressResponse response = addressService.setDefaultAddress(userId, addressId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            log.error("Error setting default address", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to set default address");
        }
    }

    /**
     * Récupérer les adresses par type (BILLING, SHIPPING, etc.)
     */
    @GetMapping("/type/{addressType}")
    public ResponseEntity<List<AddressResponse>> getAddressesByType(@PathVariable String addressType) {
        Long userId = getCurrentUserId();
        List<AddressResponse> addresses = addressService.getAddressesByType(userId, addressType);
        return ResponseEntity.ok(addresses);
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
