package com.dachser.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ShipmentFinancialRecord(
        Long id,
        Long shipmentId,
        String description,
        List<Long> incomeIds,
        List<Long> costIds,
        BigDecimal income,
        BigDecimal totalCosts,
        BigDecimal profitOrLoss,
        String currency,
        LocalDateTime calculatedAt
) {
}
