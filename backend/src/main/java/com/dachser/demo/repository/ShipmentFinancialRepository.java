package com.dachser.demo.repository;

import com.dachser.demo.dto.ShipmentFinancialRecord;
import com.dachser.demo.entity.ShipmentFinancial;
import com.dachser.demo.mapper.ShipmentFinancialMapper;
import jakarta.persistence.EntityManager;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ShipmentFinancialRepository {
    private final ShipmentFinancialMapper mapper;
    private final EntityManager entityManager;

    public ShipmentFinancialRepository(ShipmentFinancialMapper mapper, EntityManager entityManager) {
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    public List<ShipmentFinancialRecord> getShipmentFinancialsByShipmentId(Long shipmentId,
                                                                           @NonNull Integer page,
                                                                           @NonNull Integer size) {
        var query = entityManager.createQuery("""
                        SELECT s, u.username
                        FROM ShipmentFinancial s, UserEntity u
                        WHERE s.createdBy = u.id
                          AND s.shipmentId = :shipmentId
                        ORDER BY s.calculatedAt DESC
                        """,
                Object[].class);

        query.setParameter("shipmentId", shipmentId);
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        var results = query.getResultList();

        return results.stream()
                .map(row -> {
                    ShipmentFinancial entity = (ShipmentFinancial) row[0];
                    String username = (String) row[1];

                    entity.setCreatedByUsername(username);
                    return mapper.toRecord(entity);
                })
                .toList();
    }

    public Integer countShipmentFinancialsByShipmentId(Long shipmentId) {
        var query = entityManager.createQuery(
                "SELECT COUNT(s) FROM ShipmentFinancial s WHERE s.shipmentId = :shipmentId",
                Long.class);

        query.setParameter("shipmentId", shipmentId);

        return query.getSingleResult().intValue();
    }

    public ShipmentFinancialRecord saveShipmentFinancial(ShipmentFinancialRecord shipmentFinancialRecord) {
        var entity = mapper.toEntity(shipmentFinancialRecord);
        entityManager.persist(entity);
        return mapper.toRecord(entity);
    }
}
