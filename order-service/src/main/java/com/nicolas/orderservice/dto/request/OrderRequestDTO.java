package com.nicolas.orderservice.dto.request;

import com.nicolas.orderservice.enums.PaymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderRequestDTO(
        @NotNull(message = "The productId is required")
        UUID productId,
        @NotNull(message = "The quantity is required")
        Integer quantity,
        @NotNull(message = "The Payment type is required")
        PaymentType paymentType
) {
}
