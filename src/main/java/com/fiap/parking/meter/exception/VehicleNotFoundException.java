package com.fiap.parking.meter.exception;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException() {
        super("Vehicle not found");
    }
}
