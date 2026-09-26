package com.nicolas.orderservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequestDTO(
        @NotBlank(message = "Product name is required")
        String name,
        @NotNull(message = "The price is required")
        BigDecimal price,
        @NotNull(message = "The stock is required")
        Integer stock
) {
}
