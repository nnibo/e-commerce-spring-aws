package com.nicolas.event;

import java.util.UUID;

public record PaymentProcessedEvent(
        UUID orderId,
        String status,
        String message
) {
}