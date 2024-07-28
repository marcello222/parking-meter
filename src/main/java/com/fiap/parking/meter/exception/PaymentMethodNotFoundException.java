package com.fiap.parking.meter.exception;

import com.fiap.parking.meter.config.i18n.i18NConstants;

import java.util.ResourceBundle;

public class PaymentMethodNotFoundException extends RuntimeException {
    public PaymentMethodNotFoundException() {
        super(ResourceBundle.getBundle("i18n/message").getString(i18NConstants.PAYMENT_METHOD_NOT_FOUND));
    }
}
