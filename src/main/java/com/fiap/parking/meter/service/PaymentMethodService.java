package com.fiap.parking.meter.service;

import com.fiap.parking.meter.domain.PaymentMethodDto;
import com.fiap.parking.meter.entity.PaymentMethodEntity;

public interface PaymentMethodService {

    public PaymentMethodEntity createPaymentMethod(PaymentMethodDto paymentMethodDto);
}
