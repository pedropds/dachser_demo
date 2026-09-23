package com.dachser.demo.dto;

/**
 * Data Transfer Object for Shipment entity.
 */
public record ShipmentRecord(
        Long id,
        String trackingNumber,
        Long customerId,
        String customerName,
        String description,
        String status
) {}
