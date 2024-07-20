package com.fiap.parking.meter.service;

import com.fiap.parking.meter.domain.ParkingDto;
import com.fiap.parking.meter.entity.ParkingEntity;

import java.time.LocalDateTime;

public interface ParkingService {

    public ParkingEntity createParking(ParkingDto parkingDto);

    public ParkingEntity getParking(String id);

    public ParkingEntity updateParking(String id, Integer parkingDuration);

}
