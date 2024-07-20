package com.fiap.parking.meter.service.impl;

import com.fiap.parking.meter.domain.ParkingDto;
import com.fiap.parking.meter.entity.DriverEntity;
import com.fiap.parking.meter.entity.ParkingEntity;
import com.fiap.parking.meter.entity.PaymentMethodEntity;
import com.fiap.parking.meter.entity.VehicleEntity;
import com.fiap.parking.meter.enums.ParkingPeriodType;
import com.fiap.parking.meter.enums.PriceHour;
import com.fiap.parking.meter.exception.DriverNotFoundException;
import com.fiap.parking.meter.exception.ParkingNotFoundException;
import com.fiap.parking.meter.exception.PaymentMethodNotFoundException;
import com.fiap.parking.meter.exception.VehicleNotAssociatedWithDriverException;
import com.fiap.parking.meter.mapper.ParkingMapper;
import com.fiap.parking.meter.repository.DriverRepository;
import com.fiap.parking.meter.repository.ParkingRepository;
import com.fiap.parking.meter.repository.PaymentMethodRepository;
import com.fiap.parking.meter.repository.VehicleRepository;
import com.fiap.parking.meter.service.ParkingPeriodStrategy;
import com.fiap.parking.meter.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@Service
public class ParkingServiceImpl implements ParkingService {

    private final DriverRepository driverRepository;

    private final ParkingRepository parkingRepository;

    private final VehicleRepository vehicleRepository;

    private final ParkingMapper parkingMapper;

    private final ParkingPeriodStrategy fixedPeriodStrategy;

    private final ParkingPeriodStrategy perHourStrategy;

    private final PaymentMethodRepository paymentMethodRepository;

    @Autowired
    public ParkingServiceImpl(
            ParkingRepository parkingRepository,
            DriverRepository driverRepository,
            VehicleRepository vehicleRepository,
            PaymentMethodRepository paymentMethodRepository,
            ParkingMapper parkingMapper,
            @Qualifier("perHourImpl") ParkingPeriodStrategy perHourStrategy,
            @Qualifier("fixedPeriodImpl") ParkingPeriodStrategy fixedPeriodStrategy
    ) {
        this.driverRepository = driverRepository;
        this.parkingRepository = parkingRepository;
        this.vehicleRepository = vehicleRepository;
        this.parkingMapper = parkingMapper;
        this.perHourStrategy = perHourStrategy;
        this.fixedPeriodStrategy = fixedPeriodStrategy;
        this.paymentMethodRepository = paymentMethodRepository;
    }


    @Override
    public ParkingEntity createParking(ParkingDto parkingDto) {
        DriverEntity driver = driverRepository.findById(parkingDto.getDriverId()).orElseThrow(DriverNotFoundException::new);
        VehicleEntity vehicle = vehicleRepository.findById(parkingDto.getVehicleId()).orElseThrow(VehicleNotAssociatedWithDriverException::new);
        PaymentMethodEntity paymentMethod = paymentMethodRepository.findById(parkingDto.getPaymentMethodId()).orElseThrow(PaymentMethodNotFoundException::new);

        List<ParkingPeriodStrategy> strategies = Arrays.asList(fixedPeriodStrategy, perHourStrategy);

        strategies.stream()
                .filter(strategy -> strategy.supports(parkingDto.getParkingTypeCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid parking type code"))
                .applyParkingPeriod(parkingDto);

        ParkingEntity parking = parkingMapper.toEntity(parkingDto);

        parking.setVehicle(vehicle);
        parking.setPaymentMethod(paymentMethod);
        parking.setDriver(driver);
        parking = parkingRepository.save(parking);

        return parking;
    }

    @Override
    public ParkingEntity getParking(String id) {
        return parkingRepository.findById(id).orElseThrow();
    }

    @Override
    public ParkingEntity updateParking(String id, Integer parkingDuration) {
        ParkingEntity parkingEntity = parkingRepository.findById(id).orElseThrow(ParkingNotFoundException::new);

        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        LocalDateTime startDate = parkingEntity.getStartDate().atZone(zoneId).toLocalDateTime();

        LocalDateTime newEndDate = startDate.plusHours(parkingDuration);

        if (parkingEntity.getParkingTypeCode() == ParkingPeriodType.PER_HOUR.getValue()) {
            long hours = java.time.Duration.between(startDate, newEndDate).toHours();
            double value = hours * PriceHour.PRICER_HOUR.getValue();

            parkingEntity.setValue(value);
            parkingEntity.setParkingDuration(parkingDuration);
            parkingEntity.setEndDate(newEndDate);
        }

        return parkingRepository.save(parkingEntity);
    }
}
