package com.dachser.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipment_financials")
@Getter
@Setter
public class ShipmentFinancial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shipment_id", nullable = false)
    private Long shipmentId;

    @Column(name = "description")
    private String description;

    @Column(name = "income_ids", columnDefinition = "BIGINT ARRAY")
    private Long[] incomeIds;

    @Column(name = "cost_ids", columnDefinition = "BIGINT ARRAY")
    private Long[] costIds;

    @Column(name = "income", nullable = false, precision = 10, scale = 2)
    private BigDecimal income;

    @Column(name = "total_costs", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCosts;

    @Column(name = "profit_or_loss", nullable = false, precision = 10, scale = 2)
    private BigDecimal profitOrLoss;

    @Column(name = "currency", nullable = false, length = 5)
    private String currency;

    @CreationTimestamp
    @Column(name = "calculated_at", nullable = false, updatable = false)
    private LocalDateTime calculatedAt;
}