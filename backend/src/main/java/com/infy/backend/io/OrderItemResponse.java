package com.infy.backend.io;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {
    private Long orderItemId;
    private String productId;
    private String name;
    private String imageUrl;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}