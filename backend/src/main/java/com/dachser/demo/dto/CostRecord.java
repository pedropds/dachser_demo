package com.dachser.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CostRecord(
    Long id,
    Long shipmentId,
    String costType,
    BigDecimal amount,
    String status,
    String description,
    LocalDateTime createdAt
) {
}
