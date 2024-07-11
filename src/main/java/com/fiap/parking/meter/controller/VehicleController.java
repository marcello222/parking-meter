package com.fiap.parking.meter.controller;

import com.fiap.parking.meter.domain.VehicleDto;
import com.fiap.parking.meter.entity.VehicleEntity;
import com.fiap.parking.meter.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/vehicle")
@RestController
@AllArgsConstructor
@SecurityRequirement(name = "beare-key")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping()
    @Operation(summary = "Create a new vehicle")
    public ResponseEntity<VehicleEntity> createVehicle(@RequestBody VehicleDto vehicleDto) {
        VehicleEntity createVehicle = vehicleService.createVehicle(vehicleDto);
        return new ResponseEntity<>(createVehicle, HttpStatus.CREATED);
    }

    @GetMapping("/{Id}")
    @Operation(summary = "Search a vehicle by ID")
    public ResponseEntity<Optional<VehicleEntity>> getVehicle(@PathVariable("Id") String id) {
        Optional<VehicleEntity> vehicle = this.vehicleService.getById(id);
        return ResponseEntity.ok().body(vehicle);
    }

    @GetMapping()
    @Operation(summary = "Search all registered vehicles")
    public ResponseEntity<List<VehicleEntity>> getAllVehicles() {
        List<VehicleEntity> vehicles = this.vehicleService.getAllVehicles();
        return ResponseEntity.ok().body(vehicles);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a vehicle by ID")
    public ResponseEntity<Void> deleteVehicle(@PathVariable("id") String id) {
        this.vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}
