package com.fiap.parking.meter.controller;

import com.fiap.parking.meter.domain.VehicleDto;
import com.fiap.parking.meter.entity.VehicleEntity;
import com.fiap.parking.meter.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/vehicle")
@RestController
@AllArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping()
    public ResponseEntity<VehicleEntity> createVehicle(@RequestBody VehicleDto vehicleDto) {
        VehicleEntity createVehicle = vehicleService.createVehicle(vehicleDto);
        return new ResponseEntity<>(createVehicle, HttpStatus.CREATED);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<Optional<VehicleEntity>> getVehicle(@PathVariable("Id") String id) {
        Optional<VehicleEntity> vehicle = this.vehicleService.getById(id);
        return ResponseEntity.ok().body(vehicle);
    }

    @GetMapping()
    public ResponseEntity<List<VehicleEntity>> getAllVehicles() {
        List<VehicleEntity> vehicles = this.vehicleService.getAllVehicles();
        return ResponseEntity.ok().body(vehicles);
    }
}
