package com.fiap.parking.meter.service.impl;

import com.fiap.parking.meter.domain.DriverDto;
import com.fiap.parking.meter.domain.VehicleDto;
import com.fiap.parking.meter.entity.DriverEntity;
import com.fiap.parking.meter.entity.VehicleEntity;
import com.fiap.parking.meter.exception.DriverNotFoundException;
import com.fiap.parking.meter.exception.VehicleNotFoundException;
import com.fiap.parking.meter.mapper.DriverMapper;
import com.fiap.parking.meter.mapper.VehicleMapper;
import com.fiap.parking.meter.repository.DriverRepository;
import com.fiap.parking.meter.repository.VehicleRepository;
import com.fiap.parking.meter.service.DriverService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Slf4j
public class DriveServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    private final VehicleRepository vehicleRepository;

    private final DriverMapper driverMapper;

    private final VehicleMapper vehicleMapper;

    public DriveServiceImpl(DriverRepository driverRepository, DriverMapper driverMapper, VehicleRepository vehicleRepository, VehicleMapper vehicleMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    @Override
    public DriverEntity createDriver(DriverDto driverDto) {
        if(driverRepository.existsByEmail(driverDto.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists: " + driverDto.getEmail());
        }
        DriverEntity newDriver = driverMapper.toEntity(driverDto);
        DriverEntity savedDriver = driverRepository.save(newDriver);
        return savedDriver;
    }

    @Override
    public Optional<DriverEntity> getById(String id) {
        return this.driverRepository.findById(id);
    }

    @Override
    public List<DriverEntity> getAllDrivers() {
        return this.driverRepository.findAll();
    }

    @Override
    public DriverEntity updateDriver(String id, DriverDto driverDto) {
        DriverEntity driver = driverRepository.findById(id).orElseThrow(DriverNotFoundException::new);

        if (Objects.nonNull(driverDto.getEmail()) && !driverDto.getEmail().isEmpty()) {
            driver.setEmail(driverDto.getEmail());
        }
        if (Objects.nonNull(driverDto.getAddress()) && !driverDto.getAddress().isEmpty()) {
            driver.setAddress(driverDto.getAddress());
        }

        this.driverRepository.save(driver);
        return driver;
    }

    @Override
    public void deleteDriver(String id) {
        DriverEntity driver = driverRepository.findById(id).orElseThrow(DriverNotFoundException::new);
        driverRepository.delete(driver);
    }

    @Override
    public DriverEntity linkVehicleToDriver(String driverId, List<VehicleDto> vehicles) {
        DriverEntity driver = driverRepository.findById(driverId).orElseThrow(DriverNotFoundException::new);

        List<VehicleEntity> vehicleEntities = driver.getVehicles();
        if (vehicleEntities == null) {
            vehicleEntities = new ArrayList<>();
            driver.setVehicles(vehicleEntities);
        }

        vehicles.forEach(vehicleDto -> {
            VehicleEntity vehicle = vehicleMapper.toEntity(vehicleDto);
            vehicle = vehicleRepository.save(vehicle); // Save the vehicle to generate an ID
            driver.getVehicles().add(vehicle);
        });

        driverRepository.save(driver);

        return driver;
    }

}
