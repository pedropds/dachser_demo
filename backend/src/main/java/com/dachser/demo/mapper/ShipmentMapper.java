package com.dachser.demo.mapper;

import com.dachser.demo.dto.ShipmentDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ShipmentMapper implements RowMapper<ShipmentDTO> {
    
    @Override
    public ShipmentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ShipmentDTO(
            rs.getLong("id"),
            rs.getString("tracking_number"),
            rs.getLong("customer_id"),
            rs.getString("customer_name"),
            rs.getString("description"),
            rs.getString("status")
        );
    }
}