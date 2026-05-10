package com.infy.backend.service;

import java.util.List;

import com.infy.backend.io.CreateOrderRequest;
import com.infy.backend.io.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(CreateOrderRequest request);

    List<OrderResponse> getMyOrders();

    OrderResponse getOrderByOrderId(String orderId);
}