package com.dachser.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetShipmentsResponse {
    private List<ShipmentRecord> data;
    private PaginationMetadata paginationMetadata;
}