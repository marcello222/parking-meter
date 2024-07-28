package com.fiap.parking.meter.service.impl;

import com.fiap.parking.meter.config.i18n.i18NConstants;
import com.fiap.parking.meter.domain.ParkingDto;
import com.fiap.parking.meter.enums.ParkingPeriodType;
import com.fiap.parking.meter.enums.PriceHour;
import com.fiap.parking.meter.service.ParkingPeriodStrategy;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;


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
        return i18NConstants.FIXED_PERIOD_PARKING_EXPIRATION;
    }

    public void validate(ParkingDto parkingDto) {
        if (parkingDto.getParkingTypeCode() == ParkingPeriodType.FIXED_PERIOD.getValue()) {
            if (Objects.isNull(parkingDto.getParkingDuration()) || parkingDto.getParkingDuration() <= 0) {
                String errorMessageTemplate = ResourceBundle.getBundle("i18n/message").getString(i18NConstants.PARKING_DURATION_REQUIRED_FIXED_PERIOD_PARKING);
                String errorMessage = MessageFormat.format(errorMessageTemplate, parkingDto.getParkingTypeCode());
                throw new DataIntegrityViolationException(errorMessage);
            }
        }
    }

}
