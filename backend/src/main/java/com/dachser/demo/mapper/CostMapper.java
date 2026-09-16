package com.dachser.demo.mapper;

import com.dachser.demo.dto.CostRecord;
import com.dachser.demo.entity.Cost;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CostMapper {
    CostRecord toCostRecord(Cost cost);
    Cost toCostEntity(CostRecord costRecord);
}
