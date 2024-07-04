package com.fiap.parking.meter.service.impl;

import com.fiap.parking.meter.domain.VehicleDto;
import com.fiap.parking.meter.entity.DriverEntity;
import com.fiap.parking.meter.entity.VehicleEntity;
import com.fiap.parking.meter.exception.DriverNotFoundException;
import com.fiap.parking.meter.mapper.VehicleMapper;
import com.fiap.parking.meter.repository.VehicleRepository;
import com.fiap.parking.meter.service.DriverService;
import com.fiap.parking.meter.service.VehicleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleMapper vehicleMapper;

    private final VehicleRepository vehicleRepository;

    private final DriverService driverService;

    public VehicleServiceImpl(VehicleMapper vehicleMapper, VehicleRepository vehicleRepository, DriverService driverService) {
        this.vehicleMapper = vehicleMapper;
        this.vehicleRepository = vehicleRepository;
        this.driverService = driverService;
    }

    @Override
    public VehicleEntity createVehicle(VehicleDto vehicleDto) {
        DriverEntity driver = driverService.getById(vehicleDto.getDriverId()).orElseThrow(DriverNotFoundException::new);

        VehicleEntity newVehicle = vehicleMapper.toEntity(vehicleDto);
        newVehicle.setDriver(driver);

        vehicleRepository.save(newVehicle);

        return newVehicle;
    }

    @Override
    public Optional<VehicleEntity> getById(String id) {
        return vehicleRepository.findById(id);
    }

    @Override
    public List<VehicleEntity> getAllVehicles() {
        return vehicleRepository.findAll();
    }
}
