package com.fiap.parking.meter.service.impl;

import com.fiap.parking.meter.config.i18n.i18NConstants;
import com.fiap.parking.meter.domain.ParkingDto;
import com.fiap.parking.meter.entity.PaymentMethodEntity;
import com.fiap.parking.meter.enums.ParkingPeriodType;
import com.fiap.parking.meter.enums.PaymentMethodType;
import com.fiap.parking.meter.repository.PaymentMethodRepository;
import com.fiap.parking.meter.service.ParkingPeriodStrategy;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ResourceBundle;


@AllArgsConstructor
@Service("perHourImpl")
public class PerHourImpl implements ParkingPeriodStrategy {

    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public void applyParkingPeriod(ParkingDto parkingDto) {
        validate(parkingDto);
        parkingDto.setStartDate(LocalDateTime.now());
        parkingDto.setEndDate(null);
        parkingDto.setParkingDuration(null);
        parkingDto.setValue(null);
    }

    @Override
    public boolean supports(int parkingTypeCode) {
        return ParkingPeriodType.PER_HOUR.getValue() == parkingTypeCode;
    }

    @Override
    public String generateAlertMessage() {
        return i18NConstants.SYSTEM_AUTOMATICALLY_EXTEND_PARKING_ANOTHER_HOUR_UNLESS_DRIVER_TURNS_OFF_REGISTRATION;
    }


    public void validate(ParkingDto parkingDto) {
        PaymentMethodEntity paymentMethod = paymentMethodRepository.findById(parkingDto.getPaymentMethodId()).orElseThrow();
        if (parkingDto.getParkingTypeCode() == ParkingPeriodType.PER_HOUR.getValue()) {
            if (paymentMethod.getPaymentMethod() == PaymentMethodType.PIX.getValue()) {
                String errorMessageTemplate = ResourceBundle.getBundle("i18n/message").getString(i18NConstants.PAYMENT_METHOD_PIX_NOT_ALLOWED_HOURLY_PARKING);
                String errorMessage = MessageFormat.format(errorMessageTemplate, parkingDto.getParkingTypeCode());
                throw new DataIntegrityViolationException(errorMessage);
            }
        }
    }

}
