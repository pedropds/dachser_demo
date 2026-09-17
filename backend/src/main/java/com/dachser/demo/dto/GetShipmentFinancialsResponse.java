package com.dachser.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetShipmentFinancialsResponse {
    private List<ShipmentFinancialRecord> data;
    private PaginationMetadata paginationMetadata;
}
