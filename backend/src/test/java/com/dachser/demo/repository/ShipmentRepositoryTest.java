package com.dachser.demo.repository;

import com.dachser.demo.dto.GetShipmentsFilter;
import com.dachser.demo.dto.ShipmentRecord;
import com.dachser.demo.mapper.ShipmentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This test class is designed to validate the functionality of the ShipmentRepository.
 * It uses an in-memory database to ensure that the repository methods work as expected
 * without relying on an external database. Each test is isolated, and the database is reset
 * before each test to ensure consistent results.
 * <p>
 * In a real-world scenario, we would use TestContainers to spin up a temporary PostgreSQL database for more realistic
 * integration testing. However, for the sake of simplicity and speed, this test uses the in memory H2 database provided
 * by Spring Boot's @JdbcTest annotation.
 **/
@JdbcTest
@Import({ShipmentRepository.class, ShipmentMapper.class})
class ShipmentRepositoryTest {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUpData() {
        // Clean up before each test to ensure isolation
        jdbcTemplate.getJdbcTemplate().execute("DELETE FROM shipment_financials");
        jdbcTemplate.getJdbcTemplate().execute("DELETE FROM costs");
        jdbcTemplate.getJdbcTemplate().execute("DELETE FROM incomes");
        jdbcTemplate.getJdbcTemplate().execute("DELETE FROM shipments");
        jdbcTemplate.getJdbcTemplate().execute("DELETE FROM customers");

        // Insert baseline test data
        jdbcTemplate.getJdbcTemplate().execute("""
            INSERT INTO customers (id, name, tax_id) VALUES
            (1, 'Acme Corp', 'US123456789'),
            (2, 'Stark Industries', 'US987654321');
            
            INSERT INTO shipments (id, tracking_number, customer_id, status, description) VALUES 
            (101, 'TRK-0001', 1, 'DELIVERED', 'Shipment 1 for Acme'),
            (102, 'TRK-0002', 1, 'IN_TRANSIT', 'Shipment 2 for Acme'),
            (103, 'STK-0001', 2, 'CREATED', 'Shipment 1 for Stark');
        """);
    }

    @Test
    void findAllShipments_noFilters_returnsAllWithPagination() {
        GetShipmentsFilter emptyFilter = new GetShipmentsFilter(null, null, null);

        List<ShipmentRecord> results = shipmentRepository.findAllShipments(emptyFilter, 0, 2);

        assertEquals(2, results.size(), "Should return exactly 2 records based on limit");
    }

    @Test
    void findAllShipments_filterByCustomerId_returnsOnlyCustomerShipments() {
        // Filtering for Acme Corp (ID: 1)
        GetShipmentsFilter filter = new GetShipmentsFilter(null, 1L, null);

        List<ShipmentRecord> results = shipmentRepository.findAllShipments(filter, 0, 10);

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(s -> s.trackingNumber().startsWith("TRK-")));
    }

    @Test
    void findAllShipments_filterByCustomerName_returnsExactMatches() {
        GetShipmentsFilter filter = new GetShipmentsFilter("Stark Industries", null, null);

        List<ShipmentRecord> results = shipmentRepository.findAllShipments(filter, 0, 10);

        assertEquals(1, results.size());
        assertEquals("STK-0001", results.get(0).trackingNumber());
    }

    @Test
    void findAllShipments_filterBySearch_matchesTrackingNumber() {
        // The search string should match "STK" from Stark's tracking number
        GetShipmentsFilter filter = new GetShipmentsFilter(null, null, "STK");

        List<ShipmentRecord> results = shipmentRepository.findAllShipments(filter, 0, 10);

        assertEquals(1, results.size());
        assertEquals("STK-0001", results.get(0).trackingNumber());
    }

    @Test
    void countShipments_withMultipleFilters_returnsCorrectCount() {
        // Search matches Acme's ID (1) or tracking number, AND we restrict to customer ID 1
        GetShipmentsFilter filter = new GetShipmentsFilter(null, 1L, "TRK");

        Integer count = shipmentRepository.countShipments(filter);

        assertEquals(2, count);
    }
}