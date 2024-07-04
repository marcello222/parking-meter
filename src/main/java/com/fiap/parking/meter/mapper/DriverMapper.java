package com.fiap.parking.meter.mapper;

import com.fiap.parking.meter.domain.DriverDto;
import com.fiap.parking.meter.entity.DriverEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    @Mapping(target = "id", ignore = true)
    DriverEntity toEntity(DriverDto driverDto);
}
