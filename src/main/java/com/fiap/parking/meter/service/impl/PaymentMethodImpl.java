package com.fiap.parking.meter.service.impl;

import com.fiap.parking.meter.domain.PaymentMethodDto;
import com.fiap.parking.meter.entity.DriverEntity;
import com.fiap.parking.meter.entity.PaymentMethodEntity;
import com.fiap.parking.meter.enums.PaymentMethodType;
import com.fiap.parking.meter.exception.DriverNotFoundException;
import com.fiap.parking.meter.exception.PaymentMethodNotFoundException;
import com.fiap.parking.meter.mapper.PaymentMethodMapper;
import com.fiap.parking.meter.repository.DriverRepository;
import com.fiap.parking.meter.repository.PaymentMethodRepository;
import com.fiap.parking.meter.service.PaymentMethodService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class PaymentMethodImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    private final DriverRepository driverRepository;

    private final PaymentMethodMapper paymentMethodMapper;


    public PaymentMethodImpl(PaymentMethodRepository paymentMethodRepository, DriverRepository driverRepository, PaymentMethodMapper paymentMethodMapper) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.driverRepository = driverRepository;
        this.paymentMethodMapper = paymentMethodMapper;
    }

    @Override
    public PaymentMethodEntity createPaymentMethod(PaymentMethodDto paymentMethodDto) {
        DriverEntity driver = driverRepository.findById(paymentMethodDto.getDriverId()).orElseThrow(DriverNotFoundException::new);

        getPaymentMethodFromInt(paymentMethodDto.getPaymentMethod());

        PaymentMethodEntity newPaymentMethod = paymentMethodMapper.toEntity(paymentMethodDto);
        newPaymentMethod.setDriver(driver);

        this.paymentMethodRepository.save(newPaymentMethod);

        return newPaymentMethod;
    }

    @Override
    public PaymentMethodEntity updatePaymentMethod(String id, PaymentMethodDto paymentMethodDto) {
        PaymentMethodEntity paymentMethod = paymentMethodRepository.findById(id).orElseThrow(PaymentMethodNotFoundException::new);



        getPaymentMethodFromInt(paymentMethodDto.getPaymentMethod());

      if (Objects.nonNull(paymentMethodDto.getPaymentMethod())) {
          paymentMethod.setPaymentMethod(paymentMethodDto.getPaymentMethod());
        }
        if (Objects.nonNull(paymentMethodDto.getDescription())) {
            paymentMethod.setDescription(paymentMethodDto.getDescription());
        }

        this.paymentMethodRepository.save(paymentMethod);

        return paymentMethod;
    }

    public PaymentMethodType getPaymentMethodFromInt(int value) {
        return switch (value) {
            case 1 -> PaymentMethodType.CREDIT_CARD;
            case 2 -> PaymentMethodType.DEBIT_CARD;
            case 3 -> PaymentMethodType.PIX;
            default -> throw new IllegalArgumentException("Invalid PaymentMethod value: " + value);
        };
    }
}
