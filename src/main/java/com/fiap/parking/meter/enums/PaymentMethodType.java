package com.fiap.parking.meter.enums;

public enum PaymentMethodType {

    CREDIT_CARD(1),

    DEBIT_CARD(2),

    PIX(3);

    private final int value;

    PaymentMethodType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
