package com.infy.backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.infy.backend.entity.CartEntity;
import com.infy.backend.entity.CartItemEntity;
import com.infy.backend.entity.ProductEntity;
import com.infy.backend.io.CartItemRequest;
import com.infy.backend.io.CartItemResponse;
import com.infy.backend.io.CartResponse;
import com.infy.backend.repository.CartRepository;
import com.infy.backend.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

        private final CartRepository cartRepository;
        private final ProductRepository productRepository;

        @Override
        public CartResponse addToCart(String userId, CartItemRequest request) {
                CartEntity cart = cartRepository.findByUserId(userId)
                                .orElseGet(() -> cartRepository.save(CartEntity.builder()
                                                .userId(userId)
                                                .items(new ArrayList<>())
                                                .build()));

                ProductEntity product = productRepository.findByProductId(request.getProductId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Product not found"));

                CartItemEntity existingItem = cart.getItems().stream()
                                .filter(item -> item.getProduct().getProductId().equals(request.getProductId()))
                                .findFirst()
                                .orElse(null);

                if (existingItem != null) {
                        existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
                } else {
                        CartItemEntity cartItem = CartItemEntity.builder()
                                        .cart(cart)
                                        .product(product)
                                        .quantity(request.getQuantity())
                                        .build();
                        cart.getItems().add(cartItem);
                }

                cart = cartRepository.save(cart);
                return mapToCartResponse(cart);
        }

        @Override
        public CartResponse getCartByUserId(String userId) {
                CartEntity cart = cartRepository.findByUserId(userId)
                                .orElseGet(() -> cartRepository.save(CartEntity.builder()
                                                .userId(userId)
                                                .items(new ArrayList<>())
                                                .build()));

                return mapToCartResponse(cart);
        }

        @Override
        public void removeCartItem(String userId, Long cartItemId) {
                CartEntity cart = cartRepository.findByUserId(userId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

                CartItemEntity item = cart.getItems().stream()
                                .filter(cartItem -> cartItem.getId().equals(cartItemId))
                                .findFirst()
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Cart item not found"));

                cart.getItems().remove(item);
                cartRepository.save(cart);
        }

        @Override
        public CartResponse updateCartItemQuantity(String userId, Long cartItemId, Integer quantity) {
                if (quantity == null || quantity < 1) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be at least 1");
                }

                CartEntity cart = cartRepository.findByUserId(userId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

                CartItemEntity item = cart.getItems().stream()
                                .filter(cartItem -> cartItem.getId().equals(cartItemId))
                                .findFirst()
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Cart item not found"));

                item.setQuantity(quantity);

                cart = cartRepository.save(cart);
                return mapToCartResponse(cart);
        }

        private CartResponse mapToCartResponse(CartEntity cart) {
                List<CartItemResponse> itemResponses = cart.getItems().stream()
                                .map(item -> {
                                        BigDecimal price = item.getProduct().getPrice();
                                        BigDecimal subtotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));

                                        return CartItemResponse.builder()
                                                        .cartItemId(item.getId())
                                                        .productId(item.getProduct().getProductId())
                                                        .name(item.getProduct().getName())
                                                        .imageUrl(item.getProduct().getImageUrl())
                                                        .price(price)
                                                        .quantity(item.getQuantity())
                                                        .subtotal(subtotal)
                                                        .build();
                                })
                                .toList();

                BigDecimal totalAmount = itemResponses.stream()
                                .map(CartItemResponse::getSubtotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                return CartResponse.builder()
                                .cartId(cart.getId())
                                .userId(cart.getUserId())
                                .items(itemResponses)
                                .totalAmount(totalAmount)
                                .build();
        }
}