package com.infy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.backend.entity.OrderItemEntity;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}