package com.dachser.demo.dto;

/**
 * Data Transfer Object for Shipment entity.
 */
public record ShipmentDTO(
        Long id,
        String trackingNumber,
        Long customerId,
        String customerName,
        String status
) {}
