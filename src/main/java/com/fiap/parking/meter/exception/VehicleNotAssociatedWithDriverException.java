package com.fiap.parking.meter.exception;

import com.fiap.parking.meter.config.i18n.i18NConstants;

import java.util.ResourceBundle;

public class VehicleNotAssociatedWithDriverException extends RuntimeException {

    public VehicleNotAssociatedWithDriverException() {

        super(ResourceBundle.getBundle("i18n/message").getString(i18NConstants.VEHICLE_NOT_ASSOCIATED_DRIVER));

    }
}
