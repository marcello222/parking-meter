package com.fiap.parking.meter.notification;


import com.fiap.parking.meter.entity.DriverEntity;
import com.fiap.parking.meter.entity.ParkingEntity;
import com.fiap.parking.meter.notification.aws.NotificationService;
import com.fiap.parking.meter.repository.ParkingRepository;
import com.fiap.parking.meter.service.ParkingPeriodStrategy;
import com.fiap.parking.meter.service.ParkingService;
import com.fiap.parking.meter.service.aws.AwsSnsService;
import com.fiap.parking.meter.service.aws.MessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class ParkingExpirationScheduler {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ParkingRepository parkingEntityRepository;

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private List<ParkingPeriodStrategy> strategies;

    @Autowired
    private AwsSnsService awsSnsService;


    @Scheduled(fixedRate = 3600000)
    public void unifiedCheckAndNotify() {
        LocalDateTime cutoffTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());

        List<ParkingEntity> parkingLotsWithTimeExpiring = findParkingLotsWithTimeExpiring();
        List<ParkingEntity> expiredParking = parkingEntityRepository.findExpired(cutoffTime);

        notifyExpiringParkingLots(parkingLotsWithTimeExpiring);
        notifyExpiredParking(expiredParking);
    }


    //Notificação de estacionamentos que estão prestes a expirar
    private void notifyExpiringParkingLots(List<ParkingEntity> parkingLotsWithTimeExpiring) {
        parkingLotsWithTimeExpiring.forEach(parkingEntity -> {
            ParkingPeriodStrategy strategy = strategies.stream()
                    .filter(s -> s.supports(parkingEntity.getParkingTypeCode()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Unsupported parking type"));

            // Envia a notificação para os motoristas via SEs da Aws
            String alertMessage = strategy.generateAlertMessage();
            notificationService.sendToDrivers((List<DriverEntity>) parkingEntity.getDriver(), alertMessage);
            parkingService.updateParking(parkingEntity.getId(), parkingEntity.getParkingDuration());

            // Publica a mensagem no tópico SNS
            awsSnsService.publish(new MessageDto(alertMessage));

        });
    }

    //Emissão de Recibos para os motoristas
    private void notifyExpiredParking(List<ParkingEntity> expiredParking) {
        expiredParking.forEach(parkingEntity -> {
            String receiptMessage = ReceiptMessageConstructor.constructReceiptMessage(parkingEntity);

            // Envia a notificação de recibo para os motoristas.
            notificationService.sendToDrivers((List<DriverEntity>) parkingEntity.getDriver(), receiptMessage);

            // Publica a mensagem no tópico SNS
            awsSnsService.publish(new MessageDto(receiptMessage));
        });
    }

    private List<ParkingEntity> findParkingLotsWithTimeExpiring() {
        return parkingEntityRepository.findParkingLotsExpiringWithinRange();
    }
}