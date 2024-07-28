package com.fiap.parking.meter.exception;


import com.fiap.parking.meter.config.i18n.i18NConstants;

import java.util.ResourceBundle;

public class ParkingNotFoundException extends RuntimeException {
    public ParkingNotFoundException() {
        super(ResourceBundle.getBundle("i18n/message").getString(i18NConstants.PARKING_NOT_FOUND));
    }
}
