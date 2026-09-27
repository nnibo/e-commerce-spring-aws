package com.nicolas.paymentservice.events;

import com.nicolas.paymentservice.enums.PaymentStatus;

import java.util.UUID;

public record PaymentProcessedEvent(
        UUID orderId,
        PaymentStatus status,
        String message
) {}
