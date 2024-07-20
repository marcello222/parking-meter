package com.fiap.parking.meter.service;

import com.fiap.parking.meter.domain.ParkingDto;

public interface ParkingPeriodStrategy {

    void applyParkingPeriod(ParkingDto parkingDto);

    boolean supports(int parkingTypeCode);

    String generateAlertMessage();
}

