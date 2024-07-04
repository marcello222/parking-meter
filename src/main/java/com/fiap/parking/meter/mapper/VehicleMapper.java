package com.fiap.parking.meter.mapper;

import com.fiap.parking.meter.domain.VehicleDto;
import com.fiap.parking.meter.entity.VehicleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(target = "id", ignore = true)
    VehicleEntity toEntity(VehicleDto driverDto);
}
