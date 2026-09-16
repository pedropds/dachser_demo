package com.dachser.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IncomeRecord(
    Long id,
    Long shipmentId,
    BigDecimal amount,
    String status,
    LocalDateTime createdAt
) {
}
