package com.dachser.demo.service;

import com.dachser.demo.dto.CostRecord;
import com.dachser.demo.repository.CostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CostService {
    private final CostRepository costRepository;

    public CostService(CostRepository costRepository) {
        this.costRepository = costRepository;
    }

    public List<CostRecord> saveCosts(List<CostRecord> costs) {
        return costRepository.saveCosts(costs);
    }
}
