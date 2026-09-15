package com.dachser.demo.service;

import com.dachser.demo.dto.GetShipmentsRequest;
import com.dachser.demo.dto.ShipmentDTO;
import com.dachser.demo.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public List<ShipmentDTO> getShipments(GetShipmentsRequest request) {
        return this.shipmentRepository.findAllShipments(request);
    }
}
