package com.fiap.parking.meter.service.impl;

import com.fiap.parking.meter.domain.ParkingDto;
import com.fiap.parking.meter.entity.DriverEntity;
import com.fiap.parking.meter.entity.ParkingEntity;
import com.fiap.parking.meter.entity.VehicleEntity;
import com.fiap.parking.meter.enums.ParkingPeriodType;
import com.fiap.parking.meter.exception.DriverNotFoundException;
import com.fiap.parking.meter.exception.VehicleNotAssociatedWithDriverException;
import com.fiap.parking.meter.mapper.ParkingMapper;
import com.fiap.parking.meter.repository.DriverRepository;
import com.fiap.parking.meter.repository.ParkingRepository;
import com.fiap.parking.meter.repository.PaymentMethodRepository;
import com.fiap.parking.meter.repository.VehicleRepository;
import com.fiap.parking.meter.service.ParkingPeriodStrategy;
import com.fiap.parking.meter.service.ParkingService;
import com.fiap.parking.meter.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ParkingServiceImpl implements ParkingService {

    private final DriverRepository driverRepository;

    private final ParkingRepository parkingRepository;

    private final VehicleRepository vehicleRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final ParkingMapper parkingMapper;

    private final PaymentMethodService paymentMethodService;

    private final ParkingPeriodStrategy fixedPeriodStrategy;

    private final ParkingPeriodStrategy perHourStrategy;

    @Autowired
    public ParkingServiceImpl(
            ParkingRepository parkingRepository,
            DriverRepository driverRepository,
            VehicleRepository vehicleRepository,
            ParkingMapper parkingMapper,
            PaymentMethodRepository paymentMethodRepository,
            PaymentMethodService paymentMethodService,
            @Qualifier("perHourImpl") ParkingPeriodStrategy perHourStrategy,
            @Qualifier("fixedPeriodImpl") ParkingPeriodStrategy fixedPeriodStrategy
    ) {
        this.driverRepository = driverRepository;
        this.parkingRepository = parkingRepository;
        this.vehicleRepository = vehicleRepository;
        this.parkingMapper = parkingMapper;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodService = paymentMethodService;
        this.perHourStrategy = perHourStrategy;
        this.fixedPeriodStrategy = fixedPeriodStrategy;
    }


    @Override
    public ParkingEntity createParking(ParkingDto parkingDto) {
        DriverEntity driver = driverRepository.findById(parkingDto.getDriverId()).orElseThrow(DriverNotFoundException::new);
        VehicleEntity vehicle = vehicleRepository.findById(parkingDto.getVehicleId()).orElseThrow(VehicleNotAssociatedWithDriverException::new);

        ParkingEntity parking = parkingMapper.toEntity(parkingDto);

        if (parkingDto.getParkingTypeCode() == ParkingPeriodType.FIXED_PERIOD.getValue()) {
            fixedPeriodStrategy.applyParkingPeriod(parkingDto);
        } else {
            perHourStrategy.applyParkingPeriod(parkingDto);
        }

        parking.setDriver(driver);
        parking.setVehicle(vehicle);
        parking = parkingRepository.save(parking);

        return parking;
    }

    @Override
    public ParkingEntity getParking(String id) {
        return parkingRepository.findById(id).orElseThrow();
    }

}
