package com.fiap.parking.meter.exception;

public class ParkingNotFoundException extends RuntimeException {
    public ParkingNotFoundException() {
        super("Parking not found");
    }
}
