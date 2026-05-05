package com.infy.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.backend.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByProductId(String productId);

    List<ProductEntity> findByIsActiveTrue();

    List<ProductEntity> findByCategoryIgnoreCase(String category);

    List<ProductEntity> findByNameContainingIgnoreCase(String keyword);
}