package com.dachser.demo.dto;

import lombok.NonNull;

import java.util.List;

public record PaginatedResult<T>(
    List<T> data,
    @NonNull Integer totalRecords,
    @NonNull Integer totalPages,
    @NonNull Integer currentPage
) {}