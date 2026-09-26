package com.nicolas.orderservice.entity;

import com.nicolas.orderservice.enums.PaymentStatus;
import com.nicolas.orderservice.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID productId;
    private Integer quantity;
    private BigDecimal amount;
    @CreationTimestamp
    @Column(name = "order_time")
    private LocalDateTime orderTime;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
}
