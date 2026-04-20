package com.infy.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.backend.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,Long> {


    Optional<UserEntity>findByEmail(String email);
}
