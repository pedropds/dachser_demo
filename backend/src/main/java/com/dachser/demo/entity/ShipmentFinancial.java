package com.dachser.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipment_financials")
@Getter
@Setter
public class ShipmentFinancial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "shipment_id", nullable = false)
    private BigInteger shipmentId;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "income_ids")
    private BigInteger[] incomeIds;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "cost_ids")
    private BigInteger[] costIds;

    @Column(name = "income", nullable = false, precision = 10, scale = 2)
    private BigDecimal income;

    @Column(name = "total_costs", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalCosts;

    @Column(name = "profit_or_loss", nullable = false, precision = 10, scale = 2)
    private BigDecimal profitOrLoss;

    @CreationTimestamp
    @Column(name = "calculated_at", nullable = false, updatable = false)
    private LocalDateTime calculatedAt;
}
