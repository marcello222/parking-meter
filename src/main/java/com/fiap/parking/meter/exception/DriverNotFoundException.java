package com.fiap.parking.meter.exception;

public class DriverNotFoundException extends RuntimeException {
    public DriverNotFoundException() {
        super("Driver not found");
    }
}
