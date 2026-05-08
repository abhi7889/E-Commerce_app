package com.infy.backend.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.*;

import com.infy.backend.io.CartItemRequest;
import com.infy.backend.io.CartResponse;
import com.infy.backend.service.CartService;
import com.infy.backend.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1.0/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ProfileService profileService;

    @PostMapping("/items")
    public CartResponse addToCart(@RequestBody CartItemRequest request, Principal principal) {
        String email = principal.getName();
        String userId = profileService.getUserByEmail(email).getUserId();
        return cartService.addToCart(userId, request);
    }

    @GetMapping
    public CartResponse getCart(Principal principal) {
        String email = principal.getName();
        String userId = profileService.getUserByEmail(email).getUserId();
        return cartService.getCartByUserId(userId);
    }

    @DeleteMapping("/items/{cartItemId}")
    public void removeCartItem(@PathVariable Long cartItemId, Principal principal) {
        String email = principal.getName();
        String userId = profileService.getUserByEmail(email).getUserId();
        cartService.removeCartItem(userId, cartItemId);
    }
}