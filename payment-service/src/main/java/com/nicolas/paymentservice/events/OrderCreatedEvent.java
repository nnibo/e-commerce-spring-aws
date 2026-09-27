package com.nicolas.paymentservice.events;

import com.nicolas.paymentservice.enums.PaymentType;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,
        BigDecimal amount,
        PaymentType paymentType
) {}
