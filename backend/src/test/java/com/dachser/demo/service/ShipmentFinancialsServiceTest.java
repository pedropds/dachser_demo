package com.dachser.demo.service;

import com.dachser.demo.dto.CalculateFinancialsCommand;
import com.dachser.demo.dto.CostRecord;
import com.dachser.demo.dto.IncomeRecord;
import com.dachser.demo.dto.ShipmentFinancialRecord;
import com.dachser.demo.repository.ShipmentFinancialRepository;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShipmentFinancialServiceTest {

    @Mock
    private SecurityContextService securityContextService;

    @Mock
    private IncomeService incomeService;

    @Mock
    private CostService costService;

    @Mock
    private ShipmentFinancialRepository shipmentFinancialRepository;

    @InjectMocks
    private ShipmentFinancialService shipmentFinancialsService;

    @Captor
    private ArgumentCaptor<ShipmentFinancialRecord> financialRecordCaptor;

    @Test
    void calculateAndSaveSnapshot_processesCorrectly_andCalculatesTotals() {
        // 1. Arrange: Set up inputs
        Long shipmentId = 101L;
        Long mockUserId = 1L;
        String mockUsername = "system_admin";
        String expectedCurrency = "EUR";

        CalculateFinancialsCommand command = getCommand(shipmentId);

        // 2. Arrange: Mock the Security Service
        when(securityContextService.getCurrentUserId()).thenReturn(mockUserId);
        when(securityContextService.getCurrentUsername()).thenReturn(mockUsername);

        // 3. Arrange: Mock the Income and Cost services to return records WITH IDs (simulating DB save)
        List<IncomeRecord> savedIncomes = List.of(
                new IncomeRecord(10L, shipmentId, new BigDecimal("1200.00"), "ACTIVE", null, "EUR", LocalDateTime.now(), mockUserId)
        );
        List<CostRecord> savedCosts = List.of(
                new CostRecord(20L, shipmentId, "BASE", new BigDecimal("400.00"), "ACTIVE", null, "EUR", LocalDateTime.now(), mockUserId),
                new CostRecord(21L, shipmentId, "ADDITIONAL", new BigDecimal("100.00"), "ACTIVE", null, "EUR", LocalDateTime.now(), mockUserId)
        );

        when(incomeService.saveIncomes(any())).thenReturn(savedIncomes);
        when(costService.saveCosts(any())).thenReturn(savedCosts);

        // Mock the final repository save to just return whatever it is given (with a fake ID)
        when(shipmentFinancialRepository.saveShipmentFinancial(any(ShipmentFinancialRecord.class)))
                .thenAnswer(invocation -> {
                    ShipmentFinancialRecord passedRecord = invocation.getArgument(0);
                    // Returning a copy with an ID of 99L to simulate database generation
                    return new ShipmentFinancialRecord(
                            99L, passedRecord.shipmentId(), passedRecord.createdBy(), passedRecord.createdByUsername(),
                            passedRecord.description(), passedRecord.incomeIds(), passedRecord.costIds(),
                            passedRecord.income(), passedRecord.totalCosts(), passedRecord.profitOrLoss(),
                            passedRecord.currency(), passedRecord.calculatedAt()
                    );
                });

        // 4. Act
        ShipmentFinancialRecord result = shipmentFinancialsService.calculateAndSaveSnapshot(command);

        // 5. Assert: Verify the returned object
        assertNotNull(result.id());
        assertEquals(99L, result.id());
        assertEquals(mockUsername, result.createdByUsername());

        // 6. Assert: Capture what was actually passed to the repository to verify the internal business math
        verify(shipmentFinancialRepository).saveShipmentFinancial(financialRecordCaptor.capture());
        ShipmentFinancialRecord capturedRecord = financialRecordCaptor.getValue();

        // Verify the extracted IDs flow correctly
        assertEquals(List.of(10L), capturedRecord.incomeIds());
        assertEquals(List.of(20L, 21L), capturedRecord.costIds());

        // Verify the Math (1200 Income - 500 Total Costs = 700 Profit)
        assertEquals(new BigDecimal("1200.00"), capturedRecord.income());
        assertEquals(new BigDecimal("500.00"), capturedRecord.totalCosts());
        assertEquals(new BigDecimal("700.00"), capturedRecord.profitOrLoss());
        assertEquals(expectedCurrency, capturedRecord.currency());
        assertEquals("Initial calculation", capturedRecord.description());
    }

    @Nonnull
    private static CalculateFinancialsCommand getCommand(Long shipmentId) {
        CalculateFinancialsCommand.IncomeEntry incomeEntry =
                new CalculateFinancialsCommand.IncomeEntry(new BigDecimal("1200.00"), "EUR");
        CalculateFinancialsCommand.CostEntry baseCost =
                new CalculateFinancialsCommand.CostEntry("BASE", new BigDecimal("400.00"), "EUR");
        CalculateFinancialsCommand.CostEntry extraCost =
                new CalculateFinancialsCommand.CostEntry("ADDITIONAL", new BigDecimal("100.00"), "EUR");

        return new CalculateFinancialsCommand(
                shipmentId,
                List.of(incomeEntry),
                List.of(baseCost, extraCost),
                "Initial calculation"
        );
    }
}