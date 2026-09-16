package com.dachser.demo.mapper;

import com.dachser.demo.dto.IncomeRecord;
import com.dachser.demo.entity.Income;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IncomeMapper {
    IncomeRecord toIncomeRecord(Income entity);
    Income toIncomeEntity(IncomeRecord record);
}
