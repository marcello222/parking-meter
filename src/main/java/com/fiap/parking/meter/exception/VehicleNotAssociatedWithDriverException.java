package com.fiap.parking.meter.exception;

public class VehicleNotAssociatedWithDriverException extends RuntimeException {

    public VehicleNotAssociatedWithDriverException() {
        super("The vehicle is not associated with the driver");
    }
}
