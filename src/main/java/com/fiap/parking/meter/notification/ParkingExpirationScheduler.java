package com.fiap.parking.meter.notification;


import com.fiap.parking.meter.entity.ParkingEntity;
import com.fiap.parking.meter.notification.aws.NotificationService;
import com.fiap.parking.meter.repository.ParkingRepository;
import com.fiap.parking.meter.service.ParkingPeriodStrategy;
import com.fiap.parking.meter.service.ParkingService;
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

    @Scheduled(fixedRate = 3600000)
    public void unifiedCheckAndNotify() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoffTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());
        List<ParkingEntity> expiringParkingLots = parkingEntityRepository.findExpiringSoon(cutoffTime);
        List<ParkingEntity> parkingLotsWithTimeExpiring = findParkingLotsWithTimeExpiring();

        expiringParkingLots.forEach(parkingEntity -> {
            ParkingPeriodStrategy strategy = strategies.stream()
                    .filter(s -> s.supports(parkingEntity.getParkingTypeCode()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Unsupported parking type"));

            String alertMessage = strategy.generateAlertMessage();
            // Condição para enviar notificação apenas para os estacionamentos que estão prestes a expirar
            if (parkingLotsWithTimeExpiring.contains(parkingEntity)) {
                notificationService.sendToDriver(parkingEntity.getDriver(), alertMessage);
            }
            parkingService.updateParking(parkingEntity.getId(), parkingEntity.getParkingDuration());
        });
    }

    private List<ParkingEntity> findParkingLotsWithTimeExpiring() {
        LocalDateTime timeMinutesBefore = LocalDateTime.now().minusMinutes(10L);
        return parkingEntityRepository.findAllByEndDateIsNullAndStartDateLessThanEqual(timeMinutesBefore);
    }
}