package com.computerstore.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private Long id;
    private Long orderId;
    private String orderNumber;
    private String paymentMethod;
    private String senderNumber;
    private String senderName;
    private BigDecimal amount;
    private String proofImagePath;
    private String status;
    private String adminNote;
    private LocalDateTime createdAt;
}
