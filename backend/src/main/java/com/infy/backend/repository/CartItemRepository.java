package com.infy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.backend.entity.CartItemEntity;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
}