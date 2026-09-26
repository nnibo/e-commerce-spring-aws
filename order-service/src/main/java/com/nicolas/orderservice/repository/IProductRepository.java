package com.nicolas.orderservice.repository;

import com.nicolas.orderservice.entity.ProductEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IProductRepository extends JpaRepository<ProductEntity, UUID> {
    boolean existsByName(String name);
}
