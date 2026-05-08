package com.infy.backend.service;

import com.infy.backend.io.CartItemRequest;
import com.infy.backend.io.CartResponse;

public interface CartService {
    CartResponse addToCart(String userId, CartItemRequest request);

    CartResponse getCartByUserId(String userId);

    void removeCartItem(String userId, Long cartItemId);
}