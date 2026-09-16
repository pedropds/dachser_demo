package com.dachser.demo.controller;

import com.dachser.demo.dto.GetShipmentsRequest;
import com.dachser.demo.dto.GetShipmentsResponse;
import com.dachser.demo.service.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public ResponseEntity<GetShipmentsResponse> getShipments(@ModelAttribute GetShipmentsRequest request) {
        GetShipmentsResponse response = shipmentService.getShipments(request);
        return ResponseEntity.ok(response);
    }
}
