package com.fiap.parking.meter.exception;

public class PaymentMethodNotFoundException extends RuntimeException {
    public PaymentMethodNotFoundException() {
        super("Payment Method not found");
    }
}
