package com.fiap.parking.meter.enums;

import lombok.Getter;

@Getter
public enum PaymentMethodType {

    CREDIT_CARD(1),

    DEBIT_CARD(2),

    PIX(3);

    private final int value;

    PaymentMethodType(int value) {
        this.value = value;
    }

    public static PaymentMethodType getPaymentMethodFromInt(int value) {
        return switch (value) {
            case 1 -> PaymentMethodType.CREDIT_CARD;
            case 2 -> PaymentMethodType.DEBIT_CARD;
            case 3 -> PaymentMethodType.PIX;
            default -> throw new IllegalArgumentException("Invalid PaymentMethod value: " + value);
        };
    }
}
