package com.dachser.demo.repository;

import com.dachser.demo.dto.IncomeRecord;
import com.dachser.demo.entity.Income;
import com.dachser.demo.mapper.IncomeMapper;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IncomeRepository {

    private final IncomeMapper mapper;
    private final EntityManager entityManager;

    public IncomeRepository(IncomeMapper mapper, EntityManager entityManager) {
        this.mapper = mapper;
        this.entityManager = entityManager;
    }

    public List<IncomeRecord> saveIncomes(List<IncomeRecord> incomeRecords) {
        List<Income> incomeEntities = incomeRecords.stream().map(mapper::toIncomeEntity).toList();

        incomeEntities.forEach(entityManager::persist);

        return incomeEntities.stream().map(mapper::toIncomeRecord).toList();
    }
}
