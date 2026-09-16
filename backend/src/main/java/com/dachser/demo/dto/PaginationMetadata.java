package com.dachser.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationMetadata {
    private Integer totalRecords;
    private Integer totalPages;
}
