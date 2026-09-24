package com.dachser.demo.service;

import com.dachser.demo.dto.*;
import com.dachser.demo.repository.ShipmentFinancialRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShipmentFinancialService {
    private final ShipmentFinancialRepository shipmentFinancialRepository;
    private final CostService costService;
    private final IncomeService incomeService;
    private final SecurityContextService securityContextService;

    public ShipmentFinancialService(ShipmentFinancialRepository shipmentFinancialRepository,
                                    CostService costService,
                                    IncomeService incomeService, SecurityContextService securityContextService) {
        this.shipmentFinancialRepository = shipmentFinancialRepository;
        this.costService = costService;
        this.incomeService = incomeService;
        this.securityContextService = securityContextService;
    }

    public PaginatedResult<ShipmentFinancialRecord> getShipmentFinancials(Long shipmentId, Integer page, Integer size) {
        int safePage = (page != null && page >= 0) ? page : 0;
        int safeSize = (size != null && size > 0) ? size : 20;

        List<ShipmentFinancialRecord> financialRecords = shipmentFinancialRepository
                .getShipmentFinancialsByShipmentId(shipmentId, safePage, safeSize);
        Integer totalRecords = shipmentFinancialRepository.countShipmentFinancialsByShipmentId(shipmentId);

        int totalPages = (int) Math.ceil((double) totalRecords / safeSize);

        return new PaginatedResult<>(financialRecords, totalRecords, totalPages, safePage);
    }

    @Transactional
    public ShipmentFinancialRecord calculateAndSaveSnapshot(@NonNull CalculateFinancialsCommand command) {
        Long userId = securityContextService.getCurrentUserId();
        String username = securityContextService.getCurrentUsername();

        String currency = validateCurrencyConsistency(command);

        List<IncomeRecord> newIncomes = command.incomes().stream()
                .map(dto -> new IncomeRecord(
                        null,
                        command.shipmentId(),
                        dto.amount(),
                        "ACTIVE",
                        null,
                        currency,
                        LocalDateTime.now(),
                        userId)
                )
                .toList();

        List<CostRecord> newCosts = command.costs().stream()
                .map(dto -> new CostRecord(
                        null,
                        command.shipmentId(),
                        dto.costType(),
                        dto.amount(),
                        "ACTIVE",
                        null,
                        currency,
                        LocalDateTime.now(),
                        userId)
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
                userId,
                username,
                command.description(),
                savedIncomeIds,
                savedCostIds,
                totalIncomes,
                totalCosts,
                profitOrLoss,
                currency,
                LocalDateTime.now()
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

    private String validateCurrencyConsistency(CalculateFinancialsCommand command) {
        Set<String> incomeCurrencies = command.incomes().stream()
                .map(CalculateFinancialsCommand.IncomeEntry::currency)
                .collect(Collectors.toSet());

        Set<String> costCurrencies = command.costs().stream()
                .map(CalculateFinancialsCommand.CostEntry::currency)
                .collect(Collectors.toSet());

        if (incomeCurrencies.size() > 1 || costCurrencies.size() > 1) {
            throw new IllegalArgumentException("Incomes or costs have inconsistent currencies.");
        }

        String incomeCurrency = incomeCurrencies.stream().findFirst().orElseThrow(
                () -> new IllegalArgumentException("No income records found."));
        String costCurrency = costCurrencies.stream().findFirst().orElseThrow(
                () -> new IllegalArgumentException("No cost records found."));

        if (!incomeCurrency.equals(costCurrency)) {
            throw new IllegalArgumentException("Incomes and costs have different currencies.");
        }

        return incomeCurrency; // or costCurrency, since they are the same
    }
}
