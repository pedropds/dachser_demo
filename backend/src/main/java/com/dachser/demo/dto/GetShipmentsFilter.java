package com.dachser.demo.dto;

// Encapsulates just the business search criteria
public record GetShipmentsFilter(
        String customerName,
        Long customerId,
        String search // Useful for tracking_number, status, etc.
) {
}
