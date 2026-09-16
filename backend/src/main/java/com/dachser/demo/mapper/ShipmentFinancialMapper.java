package com.dachser.demo.mapper;

import com.dachser.demo.dto.ShipmentFinancialRecord;
import com.dachser.demo.entity.ShipmentFinancial;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShipmentFinancialMapper {
    ShipmentFinancial toEntity(ShipmentFinancialRecord shipmentFinancialRecord);
    ShipmentFinancialRecord toRecord(ShipmentFinancial shipmentFinancial);
}
