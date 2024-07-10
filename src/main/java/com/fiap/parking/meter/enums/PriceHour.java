package com.fiap.parking.meter.enums;


public enum PriceHour {

    PRICER_HOUR( 5.0);

    private final double value;

    PriceHour(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

}
