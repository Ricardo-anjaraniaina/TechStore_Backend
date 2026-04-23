package com.computerstore.backend.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long orderId;
    private String paymentMethod;
    private String senderNumber;
    private String senderName;
}
