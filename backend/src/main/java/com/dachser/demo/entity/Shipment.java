package com.dachser.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigInteger;

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

    @Column(name = "customer_id", nullable = false)
    Long customerId;

    @Column(name = "status", nullable = false, length = 50)
    String status = "CREATED";

    @Column(name = "description")
    private String description;
}
