package com.infy.backend.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.backend.entity.CartEntity;
import com.infy.backend.entity.OrderEntity;
import com.infy.backend.entity.OrderItemEntity;
import com.infy.backend.entity.UserEntity;
import com.infy.backend.enums.OrderStatus;
import com.infy.backend.enums.PaymentMethod;
import com.infy.backend.enums.PaymentStatus;
import com.infy.backend.io.CreateOrderRequest;
import com.infy.backend.io.OrderItemResponse;
import com.infy.backend.io.OrderResponse;
import com.infy.backend.repository.CartRepository;
import com.infy.backend.repository.OrderRepository;
import com.infy.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Override
    @Transactional
    public OrderResponse placeOrder(CreateOrderRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CartEntity cart = cartRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        PaymentMethod paymentMethod = PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());

        BigDecimal totalAmount = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderEntity order = OrderEntity.builder()
                .user(user)
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .paymentMethod(paymentMethod)
                .paymentStatus(paymentMethod == PaymentMethod.COD ? PaymentStatus.PENDING : PaymentStatus.PAID)
                .orderStatus(OrderStatus.PLACED)
                .totalAmount(totalAmount)
                .build();

        List<OrderItemEntity> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    BigDecimal price = cartItem.getProduct().getPrice();
                    Integer quantity = cartItem.getQuantity();
                    BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));

                    return OrderItemEntity.builder()
                            .order(order)
                            .productId(cartItem.getProduct().getProductId())
                            .name(cartItem.getProduct().getName())
                            .imageUrl(cartItem.getProduct().getImageUrl())
                            .price(price)
                            .quantity(quantity)
                            .subtotal(subtotal)
                            .build();
                })
                .toList();

        order.setItems(orderItems);

        OrderEntity savedOrder = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return mapToResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getMyOrders() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public OrderResponse getOrderByOrderId(String orderId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        OrderEntity order = orderRepository.findByOrderIdAndUser(orderId, user)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return mapToResponse(order);
    }

    private OrderResponse mapToResponse(OrderEntity order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .fullName(order.getFullName())
                .phone(order.getPhone())
                .addressLine1(order.getAddressLine1())
                .addressLine2(order.getAddressLine2())
                .city(order.getCity())
                .state(order.getState())
                .pincode(order.getPincode())
                .paymentMethod(order.getPaymentMethod().name())
                .paymentStatus(order.getPaymentStatus().name())
                .orderStatus(order.getOrderStatus().name())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .items(order.getItems().stream()
                        .map(item -> OrderItemResponse.builder()
                                .orderItemId(item.getId())
                                .productId(item.getProductId())
                                .name(item.getName())
                                .imageUrl(item.getImageUrl())
                                .price(item.getPrice())
                                .quantity(item.getQuantity())
                                .subtotal(item.getSubtotal())
                                .build())
                        .toList())
                .build();
    }
}