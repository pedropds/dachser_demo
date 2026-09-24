package com.dachser.demo.service;

import com.dachser.demo.dto.GetShipmentsFilter;
import com.dachser.demo.dto.PaginatedResult;
import com.dachser.demo.dto.ShipmentRecord;
import com.dachser.demo.repository.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @InjectMocks
    private ShipmentService shipmentService;

    @Test
    void getShipments_withValidPagination_returnsCorrectPaginatedResult() {
        GetShipmentsFilter filter = new GetShipmentsFilter("Acme", null, null);
        int requestedPage = 1;
        int requestedSize = 10;
        int mockTotalRecords = 25; // 25 records / 10 size = 3 pages

        List<ShipmentRecord> mockList = List.of(
                new ShipmentRecord(1L, "0001", 1L,
                        "Customer Name", "Test", "ACTIVE")
        );

        when(shipmentRepository.findAllShipments(filter, requestedPage, requestedSize)).thenReturn(mockList);
        when(shipmentRepository.countShipments(filter)).thenReturn(mockTotalRecords);

        // Act (Call the method being tested)
        PaginatedResult<ShipmentRecord> result = shipmentService.getShipments(filter, requestedPage, requestedSize);

        // Assert (Verify the outputs)
        assertEquals(1, result.currentPage());
        assertEquals(3, result.totalPages()); // Math.ceil(25 / 10)
        assertEquals(25, result.totalRecords());
        assertEquals(mockList, result.data());

        // Verify the repository was called exactly as expected
        verify(shipmentRepository).findAllShipments(filter, requestedPage, requestedSize);
        verify(shipmentRepository).countShipments(filter);
    }

    @Test
    void getShipments_withNullPagination_usesDefaults() {
        GetShipmentsFilter filter = new GetShipmentsFilter(null, null, null);

        when(shipmentRepository.findAllShipments(filter, 0, 20)).thenReturn(Collections.emptyList());
        when(shipmentRepository.countShipments(filter)).thenReturn(0);

        // Act
        PaginatedResult<ShipmentRecord> result = shipmentService.getShipments(filter, null, null);

        // Assert
        assertEquals(0, result.currentPage());
        assertEquals(0, result.totalPages());
        assertEquals(0, result.totalRecords());

        // Verify the defaults (0 and 20) were passed to the repository
        verify(shipmentRepository).findAllShipments(filter, 0, 20);
    }

    @Test
    void getShipments_withNegativePagination_forcesDefaults() {
        GetShipmentsFilter filter = new GetShipmentsFilter(null, null, null);

        when(shipmentRepository.findAllShipments(filter, 0, 20)).thenReturn(Collections.emptyList());
        when(shipmentRepository.countShipments(filter)).thenReturn(0);

        // Act (Passing negative page and zero size)
        PaginatedResult<ShipmentRecord> result = shipmentService.getShipments(filter, -5, 0);

        // Assert
        assertEquals(0, result.currentPage());

        // Verify the safeguards caught the invalid inputs and used 0 and 20
        verify(shipmentRepository).findAllShipments(filter, 0, 20);
    }

    @Test
    void getShipments_withExactPageMatch_calculatesPagesCorrectly() {
        GetShipmentsFilter filter = new GetShipmentsFilter(null, null, null);
        int mockTotalRecords = 20; // 20 records / 10 size = exactly 2 pages (no remainder)

        when(shipmentRepository.findAllShipments(filter, 0, 10)).thenReturn(Collections.emptyList());
        when(shipmentRepository.countShipments(filter)).thenReturn(mockTotalRecords);

        PaginatedResult<ShipmentRecord> result = shipmentService.getShipments(filter, 0, 10);

        assertEquals(2, result.totalPages());
    }
}
