package com.dachser.demo.controller;

import com.dachser.demo.dto.CalculateFinancialsCommand;
import com.dachser.demo.dto.ShipmentFinancialRecord;
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
    public String getShipmentFinancials(@PathVariable Long shipmentId) {
        return "Hello World";
    }

    @PostMapping("/calculate")
    public ResponseEntity<ShipmentFinancialRecord> calculateFinancialsAndSave(@RequestBody CalculateFinancialsCommand command) {
        ShipmentFinancialRecord shipmentFinancialRecord = shipmentFinancialService.calculateAndSaveSnapshot(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(shipmentFinancialRecord);
    }
}
