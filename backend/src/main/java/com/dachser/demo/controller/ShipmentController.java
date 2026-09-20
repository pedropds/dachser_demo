package com.dachser.demo.controller;

import com.dachser.demo.dto.*;
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
        PaginatedResult<ShipmentDTO> shipments = shipmentService
                .getShipments(request.filter(), request.page(), request.size());

        GetShipmentsResponse response = GetShipmentsResponse.builder()
                .data(shipments.data())
                .paginationMetadata(PaginationMetadata.builder()
                        .totalPages(shipments.totalPages())
                        .totalRecords(shipments.totalRecords())
                        .page(shipments.currentPage())
                        .build())
                .build();

        return ResponseEntity.ok(response);
    }
}
