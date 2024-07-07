package com.fiap.parking.meter.enums;

public enum ParkingPeriodType {

    FIXED_PERIOD(1),

    PER_HOUR(2);

   private final int value;

    ParkingPeriodType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
