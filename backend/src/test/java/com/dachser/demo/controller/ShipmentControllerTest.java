package com.dachser.demo.controller;

import com.dachser.demo.dto.GetShipmentsFilter;
import com.dachser.demo.dto.PaginatedResult;
import com.dachser.demo.dto.ShipmentRecord;
import com.dachser.demo.service.ShipmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShipmentController.class)
class ShipmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShipmentService shipmentService;

    @Test
    void getShipments_withValidParams_returns200AndMapsResponse() throws Exception {
        // Arrange: Prepare the mock data the service will return
        List<ShipmentRecord> mockShipments = List.of(
                new ShipmentRecord(1L, "TRK-001", 1L,
                        "Customer Name", "Test Shipment", "DELIVERED")
        );
        
        PaginatedResult<ShipmentRecord> mockPaginatedResult =
                new PaginatedResult<>(mockShipments, 1, 1, 0);

        // When the controller calls the service, return our mock data
        Mockito.when(shipmentService.getShipments(any(GetShipmentsFilter.class), eq(0), eq(10)))
               .thenReturn(mockPaginatedResult);

        // Act & Assert: Perform the simulated HTTP GET request
        mockMvc.perform(get("/api/customer/shipments")
                        // Simulating the @ModelAttribute query parameters
                        .param("page", "0")
                        .param("size", "10")
                        .param("filter.customerName", "Acme Corp")
                        .param("filter.search", "TRK"))
               // 1. Verify HTTP Status is 200 OK
               .andExpect(status().isOk())
               
               // 2. Verify the JSON Payload structure using JsonPath
               .andExpect(jsonPath("$.data").isArray())
               .andExpect(jsonPath("$.data[0].trackingNumber").value("TRK-001"))
               .andExpect(jsonPath("$.data[0].status").value("DELIVERED"))
               
               // 3. Verify the Pagination Metadata mapping
               .andExpect(jsonPath("$.paginationMetadata.page").value(0))
               .andExpect(jsonPath("$.paginationMetadata.totalPages").value(1))
               .andExpect(jsonPath("$.paginationMetadata.totalRecords").value(1));
    }

    @Test
    void getShipments_withoutParams_usesDefaultsAndReturns200() throws Exception {
        // Arrange
        PaginatedResult<ShipmentRecord> emptyResult = 
                new PaginatedResult<>(List.of(), 0, 0, 0);

        Mockito.when(shipmentService.getShipments(any(), any(), any()))
               .thenReturn(emptyResult);

        // Act & Assert: Call the endpoint with NO query parameters
        mockMvc.perform(get("/api/customer/shipments"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.data").isEmpty())
               .andExpect(jsonPath("$.paginationMetadata.totalRecords").value(0));
    }
}