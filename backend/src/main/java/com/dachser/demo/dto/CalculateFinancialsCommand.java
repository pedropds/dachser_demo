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
            @NonNull BigDecimal amount,
            String currency
    ) {
        public IncomeEntry {
            if (currency == null) currency = "EUR";
        }
    }

    public record CostEntry(
            @NonNull String costType, // e.g., "BASE_COST", "ADDITIONAL_COST"
            @NonNull BigDecimal amount,
            String currency
    ) {
        public CostEntry {
            if (currency == null) currency = "EUR";
        }
    }
}