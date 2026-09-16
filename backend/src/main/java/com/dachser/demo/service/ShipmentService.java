package com.dachser.demo.service;

import com.dachser.demo.dto.GetShipmentsRequest;
import com.dachser.demo.dto.GetShipmentsResponse;
import com.dachser.demo.dto.PaginationMetadata;
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

    public GetShipmentsResponse getShipments(GetShipmentsRequest request) {
        List<ShipmentDTO> shipments = this.shipmentRepository.findAllShipments(request);
        Integer totalRecords = this.shipmentRepository.countShipments(request);
        Integer totalPages = request.size() > 0
                ? (int) Math.ceil((double) totalRecords / request.size())
                : 0;

        return GetShipmentsResponse.builder()
                .data(shipments)
                .paginationMetadata(PaginationMetadata.builder()
                        .totalRecords(totalRecords)
                        .totalPages(totalPages)
                        .build())
                .build();
    }
}
