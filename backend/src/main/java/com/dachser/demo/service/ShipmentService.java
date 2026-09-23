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

    public PaginatedResult<ShipmentRecord> getShipments(GetShipmentsFilter filter,
                                                        Integer page,
                                                        Integer size) {
        int safePage = (page != null && page >= 0) ? page : 0;
        int safeSize = (size != null && size > 0) ? size : 20;

        List<ShipmentRecord> shipments = this.shipmentRepository.findAllShipments(filter, safePage, safeSize);
        Integer totalRecords = this.shipmentRepository.countShipments(filter);

        int totalPages = (int) Math.ceil((double) totalRecords / safeSize);

        return new PaginatedResult<>(shipments, totalRecords, totalPages, safePage);
    }
}
