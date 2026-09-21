package com.dachser.demo.dto;

import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;

public record CalculateFinancialsCommand(
        @NonNull Long shipmentId,
        @NonNull List<IncomeEntry> incomes,
        @NonNull List<CostEntry> costs,
        @NonNull String description
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