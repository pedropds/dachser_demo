package com.dachser.demo.repository;

import com.dachser.demo.dto.GetShipmentsFilter;
import com.dachser.demo.dto.GetShipmentsRequest;
import com.dachser.demo.dto.ShipmentDTO;
import com.dachser.demo.mapper.ShipmentMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ShipmentRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ShipmentMapper mapper;

    public ShipmentRepository(NamedParameterJdbcTemplate jdbcTemplate, ShipmentMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    public List<ShipmentDTO> findAllShipments(GetShipmentsRequest request) {
        StringBuilder sql = new StringBuilder("""
                    SELECT s.id,
                        s.tracking_number,
                        s.customer_id,
                        c.name AS customer_name,
                        s.status
                    FROM shipments s
                    JOIN customers c ON s.customer_id = c.id
                    WHERE 1=1
                """);

        MapSqlParameterSource parameters = new MapSqlParameterSource();

        // Dynamic filtering
        appendFilters(sql, parameters, request);

        //TODO: add dynamic ordering as well

        // Pagination
        sql.append(" LIMIT :limit OFFSET :offset");
        parameters.addValue("limit", request.size());
        parameters.addValue("offset", request.page() * request.size());

        return jdbcTemplate.query(sql.toString(), parameters, mapper);
    }

    private void appendFilters(StringBuilder sql,
                               MapSqlParameterSource parameters,
                               GetShipmentsRequest request) {
        GetShipmentsFilter filter = request.filter();

        if (filter.customerId() != null) {
            sql.append(" AND s.customer_id = :customer_id");
            parameters.addValue("customer_id", filter.customerId());
        }

        if (filter.customerName() != null && !filter.customerName().isEmpty()) {
            sql.append(" AND c.name = :customer_name");
            parameters.addValue("customer_name", filter.customerName());
        }

        // Very basic search functionality for now, just to exemplify
        if (filter.search() != null && !filter.search().isEmpty()) {
            sql.append("""
                    AND (
                        s.tracking_number ILIKE :search OR
                        c.name ILIKE :search OR
                        s.customer_id::text ILIKE :search)
                    """);
            parameters.addValue("search", "%" + filter.search() + "%");
        }
    }
}
