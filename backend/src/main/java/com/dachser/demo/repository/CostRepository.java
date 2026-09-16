package com.dachser.demo.repository;

import com.dachser.demo.dto.CostRecord;
import com.dachser.demo.entity.Cost;
import com.dachser.demo.mapper.CostMapper;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CostRepository {

    private final CostMapper mapper;
    private final EntityManager entityManager;

    public CostRepository(CostMapper mapper, EntityManager entityManager) {
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    public List<CostRecord> saveCosts(List<CostRecord> costs) {
        List<Cost> costEntities = costs.stream().map(mapper::toCostEntity).toList();

        costEntities.forEach(entityManager::persist);

        return costEntities.stream().map(mapper::toCostRecord).toList();
    }
}
