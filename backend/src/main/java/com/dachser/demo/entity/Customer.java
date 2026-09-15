package com.dachser.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "customers")
@Getter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "tax_id", nullable = false, length = 50)
    String taxId;
}
