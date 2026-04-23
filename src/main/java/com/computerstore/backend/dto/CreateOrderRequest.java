package com.computerstore.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    @NotNull(message = "Shipping address ID is required")
    private Long shippingAddressId;

    @NotNull(message = "Billing address ID is required")
    private Long billingAddressId;

    private String paymentMethod; // CREDIT_CARD, DEBIT_CARD, PAYPAL

    private String notes;

    // Si false, les articles du panier sont utilisés
    // Si true, spécifiez les items manuellement
    private Boolean useCartItems = true;
}
