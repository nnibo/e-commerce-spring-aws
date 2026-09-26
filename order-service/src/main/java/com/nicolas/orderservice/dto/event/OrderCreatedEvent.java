package com.nicolas.orderservice.dto.event;

import com.nicolas.orderservice.enums.PaymentType;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,
        BigDecimal amount,
        PaymentType paymentType
) {
}
