package com.dachser.demo.service;

import com.dachser.demo.dto.*;
import com.dachser.demo.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public PaginatedResult<ShipmentDTO> getShipments(GetShipmentsFilter filter,
                                                     Integer page,
                                                     Integer size) {
        List<ShipmentDTO> shipments = this.shipmentRepository.findAllShipments(filter, page, size);
        Integer totalRecords = this.shipmentRepository.countShipments(filter);
        Integer totalPages = size > 0
                ? (int) Math.ceil((double) totalRecords / size)
                : 0;

        return new PaginatedResult<>(shipments, totalRecords, totalPages, page);
    }
}
