package com.fiap.parking.meter.service.impl;

import com.fiap.parking.meter.domain.ParkingDto;
import com.fiap.parking.meter.entity.PaymentMethodEntity;
import com.fiap.parking.meter.enums.ParkingPeriodType;
import com.fiap.parking.meter.enums.PaymentMethodType;
import com.fiap.parking.meter.repository.PaymentMethodRepository;
import com.fiap.parking.meter.service.ParkingPeriodStrategy;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service("perHourImpl")
public class PerHourImpl implements ParkingPeriodStrategy {

    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public void applyParkingPeriod(ParkingDto parkingDto) {
        validate(parkingDto);
        //TODO implementar lógica de cálculo de valor por hora
    }

    public void validate(ParkingDto parkingDto) {
        PaymentMethodEntity paymentMethod = paymentMethodRepository.findById(parkingDto.getPaymentMethodId()).orElseThrow();
        if (parkingDto.getParkingTypeCode() == ParkingPeriodType.PER_HOUR.getValue()) {
            if (paymentMethod.getPaymentMethod() == PaymentMethodType.PIX.getValue()) {
                throw new DataIntegrityViolationException("Payment method PIX is not allowed for hourly parking");
            }
        }
    }
}
