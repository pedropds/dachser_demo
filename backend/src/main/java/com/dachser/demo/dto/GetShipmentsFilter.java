package com.dachser.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Encapsulates just the business search criteria
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetShipmentsFilter {
    String customerName;
    Long customerId;
    String search; // Useful for tracking_number, status, etc.
}
