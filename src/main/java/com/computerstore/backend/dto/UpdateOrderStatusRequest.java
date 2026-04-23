package com.computerstore.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {
    private String status; // PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED

    private String trackingNumber; // Optionnel pour SHIPPED
}
