package com.dachser.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * Entity class representing a Shipment.
 */
@Entity
@Table(name = "shipments")
@Getter
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "tracking_number", nullable = false, length = 50)
    String trackingNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @Column(name = "status", nullable = false, length = 50)
    String status = "CREATED";
}
