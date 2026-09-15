package com.dachser.demo.controller;

import com.dachser.demo.dto.GetShipmentsRequest;
import com.dachser.demo.dto.GetShipmentsResponse;
import com.dachser.demo.dto.ShipmentDTO;
import com.dachser.demo.service.ShipmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customer/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public GetShipmentsResponse getShipments(@ModelAttribute GetShipmentsRequest request) {
        List<ShipmentDTO> shipments = shipmentService.getShipments(request);

        return GetShipmentsResponse.builder()
                .data(shipments)
                .build();
    }
}
