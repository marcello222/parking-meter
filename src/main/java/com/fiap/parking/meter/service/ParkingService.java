package com.fiap.parking.meter.service;

import com.fiap.parking.meter.domain.ParkingDto;
import com.fiap.parking.meter.entity.ParkingEntity;

public interface ParkingService {

    public ParkingEntity createParking(ParkingDto parkingDto);

}
