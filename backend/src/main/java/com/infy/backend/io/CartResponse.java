package com.infy.backend.io;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartResponse {
    private Long cartId;
    private String userId;
    private List<CartItemResponse> items;
    private BigDecimal totalAmount;
}