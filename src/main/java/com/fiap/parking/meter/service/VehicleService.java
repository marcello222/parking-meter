package com.fiap.parking.meter.service;

import com.fiap.parking.meter.domain.VehicleDto;
import com.fiap.parking.meter.entity.VehicleEntity;

import java.util.List;
import java.util.Optional;


public interface VehicleService {

    public VehicleEntity createVehicle(VehicleDto vehicleDto);

    public Optional<VehicleEntity> getById(String id);

    public List<VehicleEntity> getAllVehicles();

    public void deleteVehicle(String id);

}
