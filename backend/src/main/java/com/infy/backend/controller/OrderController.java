package com.infy.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.infy.backend.io.CreateOrderRequest;
import com.infy.backend.io.OrderResponse;
import com.infy.backend.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@RequestBody CreateOrderRequest request) {
        return orderService.placeOrder(request);
    }

    @GetMapping
    public List<OrderResponse> getMyOrders() {
        return orderService.getMyOrders();
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrderByOrderId(@PathVariable String orderId) {
        return orderService.getOrderByOrderId(orderId);
    }
}