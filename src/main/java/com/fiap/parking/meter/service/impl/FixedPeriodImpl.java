package com.fiap.parking.meter.service.impl;

import com.fiap.parking.meter.domain.ParkingDto;
import com.fiap.parking.meter.enums.ParkingPeriodType;
import com.fiap.parking.meter.enums.PriceHour;
import com.fiap.parking.meter.service.ParkingPeriodStrategy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service("fixedPeriodImpl")
public class FixedPeriodImpl implements ParkingPeriodStrategy {

    @Override
    public void applyParkingPeriod(ParkingDto parkingDto) {
        validate(parkingDto);
        parkingDto.setStartDate(LocalDateTime.now());
        parkingDto.setEndDate(parkingDto.getStartDate().plusHours(parkingDto.getParkingDuration()));
        parkingDto.setValue(parkingDto.getParkingDuration() * PriceHour.PRICER_HOUR.getValue());
    }


    public void validate(ParkingDto parkingDto) {
        if (parkingDto.getParkingTypeCode() == ParkingPeriodType.FIXED_PERIOD.getValue()) {
            if (Objects.isNull(parkingDto.getParkingDuration()) || parkingDto.getParkingDuration() <= 0) {
                throw new DataIntegrityViolationException("Parking duration is required for fixed period parking");
            }
        }
    }
}
