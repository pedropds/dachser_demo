package com.dachser.demo.repository;

import com.dachser.demo.dto.ShipmentFinancialRecord;
import com.dachser.demo.mapper.ShipmentFinancialMapper;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class ShipmentFinancialRepository {
    private final ShipmentFinancialMapper mapper;
    private final EntityManager entityManager;

    public ShipmentFinancialRepository(ShipmentFinancialMapper mapper, EntityManager entityManager) {
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    public ShipmentFinancialRecord saveShipmentFinancial(ShipmentFinancialRecord shipmentFinancialRecord) {
        var entity = mapper.toEntity(shipmentFinancialRecord);
        entityManager.persist(entity);
        return mapper.toRecord(entity);
    }
}
