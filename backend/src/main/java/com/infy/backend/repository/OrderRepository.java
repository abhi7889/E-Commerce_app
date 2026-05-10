package com.infy.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.backend.entity.OrderEntity;
import com.infy.backend.entity.UserEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserOrderByCreatedAtDesc(UserEntity user);

    Optional<OrderEntity> findByOrderIdAndUser(String orderId, UserEntity user);
}