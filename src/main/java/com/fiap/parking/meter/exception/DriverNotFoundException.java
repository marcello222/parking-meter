package com.fiap.parking.meter.exception;

import com.fiap.parking.meter.config.i18n.i18NConstants;

import java.util.ResourceBundle;

public class DriverNotFoundException extends RuntimeException {
    public DriverNotFoundException() {
        super(ResourceBundle.getBundle("i18n/message").getString(i18NConstants.DRIVER_NOT_FOUND));
    }
}
