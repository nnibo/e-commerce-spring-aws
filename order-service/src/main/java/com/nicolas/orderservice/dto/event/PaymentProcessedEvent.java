package com.nicolas.orderservice.dto.event;

import com.nicolas.orderservice.enums.PaymentStatus;

import java.util.UUID;

public record PaymentProcessedEvent(
        UUID orderId,
        PaymentStatus status,
        String message
) {
}
