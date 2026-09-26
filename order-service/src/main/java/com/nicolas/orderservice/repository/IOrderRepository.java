package com.nicolas.orderservice.repository;

import com.nicolas.orderservice.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IOrderRepository extends JpaRepository<OrderEntity, UUID> {
}
