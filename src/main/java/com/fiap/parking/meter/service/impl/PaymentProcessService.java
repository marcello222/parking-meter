package com.fiap.parking.meter.service.impl;

import com.fiap.parking.meter.domain.ParkingDto;
import com.fiap.parking.meter.entity.PaymentMethodEntity;
import com.fiap.parking.meter.exception.PaymentMethodNotFoundException;
import com.fiap.parking.meter.repository.PaymentMethodRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentProcessService {

    private final PaymentMethodRepository paymentMethodRepository;

    public void paymentProcess(ParkingDto parkingDto) {

        PaymentMethodEntity paymentMethod = paymentMethodRepository.findById(parkingDto.getPaymentMethodId()).orElseThrow(PaymentMethodNotFoundException::new);

        log.info("Paid with method: ", paymentMethod.getPaymentMethod());
    }
}
