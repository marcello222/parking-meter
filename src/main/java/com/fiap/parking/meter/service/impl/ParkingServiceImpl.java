package com.fiap.parking.meter.service.impl;

import com.fiap.parking.meter.domain.ParkingDto;
import com.fiap.parking.meter.entity.DriverEntity;
import com.fiap.parking.meter.entity.ParkingEntity;
import com.fiap.parking.meter.entity.VehicleEntity;
import com.fiap.parking.meter.exception.DriverNotFoundException;
import com.fiap.parking.meter.exception.VehicleNotAssociatedWithDriverException;
import com.fiap.parking.meter.mapper.ParkingMapper;
import com.fiap.parking.meter.repository.DriverRepository;
import com.fiap.parking.meter.repository.ParkingRepository;
import com.fiap.parking.meter.repository.PaymentMethodRepository;
import com.fiap.parking.meter.repository.VehicleRepository;
import com.fiap.parking.meter.service.ParkingService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ParkingServiceImpl implements ParkingService {

    private final DriverRepository driverRepository;

    private final ParkingRepository parkingRepository;

    private final VehicleRepository vehicleRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final ParkingMapper parkingMapper;

    public ParkingServiceImpl(ParkingRepository parkingRepository, DriverRepository driverRepository, VehicleRepository vehicleRepository,
                              ParkingMapper parkingMapper, PaymentMethodRepository paymentMethodRepository) {
        this.driverRepository = driverRepository;
        this.parkingRepository = parkingRepository;
        this.vehicleRepository = vehicleRepository;
        this.parkingMapper = parkingMapper;
        this.paymentMethodRepository = paymentMethodRepository;
    }


    @Override
    public ParkingEntity createParking(ParkingDto parkingDto) {
        DriverEntity driver = driverRepository.findById(parkingDto.getDriverId()).orElseThrow(DriverNotFoundException::new);

        if (vehicleRepository.findById(parkingDto.getVehicleId()).isEmpty()) {
            throw new DataIntegrityViolationException("The vehicle is not associated with the driver = " + parkingDto.getVehicleId());
        }

        VehicleEntity vehicle = vehicleRepository.findById(parkingDto.getVehicleId()).orElseThrow(VehicleNotAssociatedWithDriverException::new);

        ParkingEntity parking = parkingMapper.toEntity(parkingDto);

        parking.setDriver(driver);
        parking.setVehicle(vehicle);
        parking = parkingRepository.save(parking);

        return parking;
    }
}
