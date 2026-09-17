package com.dachser.demo.controller;

import com.dachser.demo.dto.*;
import com.dachser.demo.service.ShipmentFinancialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financials/shipments")
public class ShipmentFinancialController {

    private final ShipmentFinancialService shipmentFinancialService;

    public ShipmentFinancialController(ShipmentFinancialService shipmentFinancialService) {
        this.shipmentFinancialService = shipmentFinancialService;
    }

    @GetMapping("/{shipmentId}")
    public ResponseEntity<GetShipmentFinancialsResponse> getShipmentFinancials(@ModelAttribute GetShipmentFinancialsRequest request) {
        PaginatedResult<ShipmentFinancialRecord> result = shipmentFinancialService
                .getShipmentFinancials(request.shipmentId(), request.page(), request.size());

        GetShipmentFinancialsResponse response = GetShipmentFinancialsResponse.builder()
                .data(result.data())
                .paginationMetadata(PaginationMetadata.builder()
                        .totalPages(result.totalPages())
                        .totalRecords(result.totalRecords())
                        .build())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/calculate")
    public ResponseEntity<ShipmentFinancialRecord> calculateFinancialsAndSave(@RequestBody CalculateFinancialsCommand command) {
        ShipmentFinancialRecord shipmentFinancialRecord = shipmentFinancialService.calculateAndSaveSnapshot(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(shipmentFinancialRecord);
    }
}
