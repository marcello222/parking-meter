package com.fiap.parking.meter.service;


import com.fiap.parking.meter.domain.DriverDto;
import com.fiap.parking.meter.domain.VehicleDto;
import com.fiap.parking.meter.entity.DriverEntity;

import java.util.List;
import java.util.Optional;

public interface DriverService {

    public DriverEntity createDriver(DriverDto driverDto);

    public Optional<DriverEntity> getById(String id);

    public List<DriverEntity> getAllDrivers();

    public DriverEntity updateDriver(String id, DriverDto driverDto);

    public void deleteDriver(String id);

    DriverEntity linkVehicleToDriver(String driverId, List<VehicleDto> vehicles);

}
