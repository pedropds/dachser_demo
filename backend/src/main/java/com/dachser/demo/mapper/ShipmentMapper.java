package com.dachser.demo.mapper;

import com.dachser.demo.dto.ShipmentRecord;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ShipmentMapper implements RowMapper<ShipmentRecord> {
    
    @Override
    public ShipmentRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ShipmentRecord(
            rs.getLong("id"),
            rs.getString("tracking_number"),
            rs.getLong("customer_id"),
            rs.getString("customer_name"),
            rs.getString("description"),
            rs.getString("status")
        );
    }
}