package com.fiap.parking.meter.mapper;

import com.fiap.parking.meter.domain.ParkingDto;
import com.fiap.parking.meter.entity.ParkingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParkingMapper {

    @Mapping(target = "id", ignore = true)
    ParkingEntity toEntity(ParkingDto parkingDto);

}
