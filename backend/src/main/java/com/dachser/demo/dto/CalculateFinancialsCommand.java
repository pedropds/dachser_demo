package com.dachser.demo.dto;

import java.math.BigDecimal;
import java.util.List;

public record CalculateFinancialsCommand(
        Long shipmentId,
        List<IncomeEntry> incomes,
        List<CostEntry> costs
) {
    // Nested records to strictly define the expected input shapes
    public record IncomeEntry(
            BigDecimal amount
    ) {}

    public record CostEntry(
            String costType, // e.g., "BASE_COST", "ADDITIONAL_COST"
            BigDecimal amount
    ) {}
}