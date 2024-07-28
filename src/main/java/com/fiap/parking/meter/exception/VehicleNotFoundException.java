package com.fiap.parking.meter.exception;

import com.fiap.parking.meter.config.i18n.i18NConstants;

import java.util.ResourceBundle;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException() {
        super(ResourceBundle.getBundle("i18n/message").getString(i18NConstants.VEHICLE_NOT_FOUND));
    }
}
