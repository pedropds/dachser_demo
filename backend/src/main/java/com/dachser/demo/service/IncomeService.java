package com.dachser.demo.service;

import com.dachser.demo.dto.IncomeRecord;
import com.dachser.demo.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeService {
    private final IncomeRepository incomeRepository;

    public IncomeService(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    public List<IncomeRecord> saveIncomes(List<IncomeRecord> incomeRecords) {
        return incomeRepository.saveIncomes(incomeRecords);
    }
}
