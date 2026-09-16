package com.dachser.demo.service;

import com.dachser.demo.dto.CalculateFinancialsCommand;
import com.dachser.demo.dto.CostRecord;
import com.dachser.demo.dto.IncomeRecord;
import com.dachser.demo.dto.ShipmentFinancialRecord;
import com.dachser.demo.repository.ShipmentFinancialRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ShipmentFinancialService {
    private final ShipmentFinancialRepository shipmentFinancialRepository;
    private final CostService costService;
    private final IncomeService incomeService;

    public ShipmentFinancialService(ShipmentFinancialRepository shipmentFinancialRepository,
                                    CostService costService,
                                    IncomeService incomeService) {
        this.shipmentFinancialRepository = shipmentFinancialRepository;
        this.costService = costService;
        this.incomeService = incomeService;
    }

    @Transactional
    public ShipmentFinancialRecord calculateAndSaveSnapshot(@NonNull CalculateFinancialsCommand command) {
        List<IncomeRecord> newIncomes = command.incomes().stream()
                .map(dto -> new IncomeRecord(
                        null,
                        command.shipmentId(),
                        dto.amount(),
                        "ACTIVE",
                        null)
                )
                .toList();

        List<CostRecord> newCosts = command.costs().stream()
                .map(dto -> new CostRecord(
                        null,
                        command.shipmentId(),
                        dto.costType(),
                        dto.amount(),
                        "ACTIVE",
                        null)
                )
                .toList();

        // Persist the new incomes and costs using their respective services
        List<IncomeRecord> savedIncomes = incomeService.saveIncomes(newIncomes);
        List<CostRecord> savedCosts = costService.saveCosts(newCosts);

        // Extract the IDs of the saved incomes and costs
        List<Long> savedIncomeIds = savedIncomes.stream().map(IncomeRecord::id).toList();
        List<Long> savedCostIds = savedCosts.stream().map(CostRecord::id).toList();

        // Calculate total incomes and costs
        BigDecimal totalIncomes = calculateTotalIncomes(savedIncomes);
        BigDecimal totalCosts = calculateTotalCosts(savedCosts);
        BigDecimal profitOrLoss = totalIncomes.subtract(totalCosts);

        // Create a new ShipmentFinancialRecord with the calculated values and persist it.
        ShipmentFinancialRecord financialRecord = new ShipmentFinancialRecord(
                null,
                command.shipmentId(),
                savedIncomeIds,
                savedCostIds,
                totalIncomes,
                totalCosts,
                profitOrLoss,
                null
        );

        return shipmentFinancialRepository.saveShipmentFinancial(financialRecord);
    }

    private BigDecimal calculateTotalIncomes(@NonNull List<IncomeRecord> incomes) {
        return incomes.stream()
                .map(IncomeRecord::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalCosts(@NonNull List<CostRecord> costs) {
        return costs.stream()
                .map(CostRecord::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
