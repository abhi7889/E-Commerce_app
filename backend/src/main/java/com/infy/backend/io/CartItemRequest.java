package com.infy.backend.io;

import lombok.Data;

@Data
public class CartItemRequest {
    private String productId;
    private Integer quantity;
}