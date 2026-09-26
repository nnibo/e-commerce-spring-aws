package com.nicolas.orderservice.dto.response;

import com.nicolas.orderservice.enums.PaymentStatus;
import com.nicolas.orderservice.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponseDTO(
        UUID id,
        UUID productId,
        Integer quantity,
        BigDecimal amount,
        LocalDateTime orderTime,
        PaymentStatus paymentStatus,
        PaymentType paymentType
) {
}