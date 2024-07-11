package com.fiap.parking.meter.controller;

import com.fiap.parking.meter.domain.DriverDto;
import com.fiap.parking.meter.domain.VehicleDto;
import com.fiap.parking.meter.entity.DriverEntity;
import com.fiap.parking.meter.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/driver")
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class DriverController {

    private final DriverService driverService;

    @PostMapping()
    @Operation(summary = "create a driver")
    public ResponseEntity<DriverEntity> createDriver(@RequestBody DriverDto driverDto) {
        DriverEntity createDriver = driverService.createDriver(driverDto);
        return new ResponseEntity<>(createDriver, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Search for a driver by ID")
    public ResponseEntity<Optional<DriverEntity>> getDriver(@PathVariable("id") String id) {
        Optional<DriverEntity> driver = this.driverService.getById(id);
        return ResponseEntity.ok().body(driver);
    }

    @GetMapping()
    @Operation(summary = "Search all drivers")
    public ResponseEntity<List<DriverEntity>> getAllDrivers() {
        List<DriverEntity> drivers = this.driverService.getAllDrivers();
        return ResponseEntity.ok().body(drivers);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a driver by ID")
    public ResponseEntity<DriverEntity> updateDriver(@PathVariable("id") String id, @RequestBody DriverDto driverDto) {
        DriverEntity updatedDriver = this.driverService.updateDriver(id, driverDto);
        return ResponseEntity.ok().body(updatedDriver);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a driver by ID")
    public ResponseEntity<Void> deleteDriver(@PathVariable("id") String id) {
        this.driverService.deleteDriver(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/vehicles")
    @Operation(summary = "Create a driver list")
    public ResponseEntity<DriverEntity> linkVehicleToDriver(@PathVariable("id") String id, @RequestBody List<VehicleDto> vehicleIds) {
        return ResponseEntity.ok(this.driverService.linkVehicleToDriver(id, vehicleIds));
    }


}
