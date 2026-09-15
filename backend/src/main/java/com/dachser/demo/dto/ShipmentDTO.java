package com.dachser.demo.dto;

public record ShipmentDTO(
        Long id,
        String trackingNumber,
        Long customerId,
        String customerName,
        String status
) {}
