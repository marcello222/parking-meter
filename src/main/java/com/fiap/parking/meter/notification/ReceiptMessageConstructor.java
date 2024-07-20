package com.fiap.parking.meter.notification;

import com.fiap.parking.meter.entity.ParkingEntity;

public class ReceiptMessageConstructor {

    public static String constructReceiptMessage(ParkingEntity parkingEntity) {
        String driverName = parkingEntity.getDriver().getName();

        String message = String.format(
                "Recibo da Sessão de Estacionamento:\n" +
                        "Motorista: %s\n" +
                        "Hora de Início: %s\n" +
                        "Hora de Término: %s\n" +
                        "Duração: %d minutos\n" +
                        "Custo Total: $%.2f",
                driverName,
                parkingEntity.getStartDate().toString(),
                parkingEntity.getEndDate().toString(),
                parkingEntity.getParkingDuration(),
                parkingEntity.getValue()
        );
        return message;
    }

}
