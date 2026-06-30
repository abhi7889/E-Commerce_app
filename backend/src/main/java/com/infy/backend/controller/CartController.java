package com.infy.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.infy.backend.entity.UserEntity;
import com.infy.backend.io.CartItemRequest;
import com.infy.backend.io.CartResponse;
import com.infy.backend.repository.UserRepository;
import com.infy.backend.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    private String getUserIdFromAuth(Authentication authentication) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getUserId();
    }

    @PostMapping
    public ResponseEntity<CartResponse> addToCart(
            Authentication authentication,
            @RequestBody CartItemRequest request) {
        String userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(cartService.addToCart(userId, request));
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(Authentication authentication) {
        String userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(
            Authentication authentication,
            @PathVariable Long cartItemId) {
        String userId = getUserIdFromAuth(authentication);
        cartService.removeCartItem(userId, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartResponse> updateCartItemQuantity(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestBody CartItemRequest request) {
        String userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(
                cartService.updateCartItemQuantity(userId, cartItemId, request.getQuantity()));
    }
}