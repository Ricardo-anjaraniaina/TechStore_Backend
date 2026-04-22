package com.computerstore.backend.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private Long shippingAddressId;
    private Long billingAddressId;
}
