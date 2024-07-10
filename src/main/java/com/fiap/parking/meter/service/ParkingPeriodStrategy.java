package com.fiap.parking.meter.service;

import com.fiap.parking.meter.domain.ParkingDto;
import com.fiap.parking.meter.enums.ParkingPeriodType;

public interface ParkingPeriodStrategy {

    void applyParkingPeriod(ParkingDto parkingDto);

}

