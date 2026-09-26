package com.nicolas.orderservice.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponseDTO(
        UUID id,
        String name,
        BigDecimal price,
        Integer stock
) {
}
