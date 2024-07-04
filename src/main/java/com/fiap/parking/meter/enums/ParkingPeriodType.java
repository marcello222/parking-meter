package com.fiap.parking.meter.enums;

public enum ParkingPeriodType {

    FIXED_PERIOD("fixedPeriod"),

    PER_HOUR("perHour");

    private final String description;

    ParkingPeriodType(String description) {
        this.description = description;
    }
}
