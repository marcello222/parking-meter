package com.fiap.parking.meter.service.impl;

import com.fiap.parking.meter.domain.ParkingDto;
import com.fiap.parking.meter.enums.ParkingPeriodType;
import com.fiap.parking.meter.enums.PriceHour;
import com.fiap.parking.meter.service.ParkingPeriodStrategy;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;


@AllArgsConstructor
@Service("fixedPeriodImpl")
public class FixedPeriodImpl implements ParkingPeriodStrategy {


    @Override
    public void applyParkingPeriod(ParkingDto parkingDto) {
        validate(parkingDto);
        parkingDto.setStartDate(LocalDateTime.now());
        parkingDto.setEndDate(parkingDto.getStartDate().plusHours(parkingDto.getParkingDuration()));
        parkingDto.setValue(parkingDto.getParkingDuration() * PriceHour.PRICER_HOUR.getValue());
    }

    @Override
    public boolean supports(int parkingTypeCode) {
        return ParkingPeriodType.FIXED_PERIOD.getValue() == parkingTypeCode;
    }

    @Override
    public String generateAlertMessage() {
        return "Your fixed period parking is about to expire.";
    }




    public void validate(ParkingDto parkingDto) {
        if (parkingDto.getParkingTypeCode() == ParkingPeriodType.FIXED_PERIOD.getValue()) {
            if (Objects.isNull(parkingDto.getParkingDuration()) || parkingDto.getParkingDuration() <= 0) {
                throw new DataIntegrityViolationException("Parking duration is required for fixed period parking");
            }
        }
    }
}
