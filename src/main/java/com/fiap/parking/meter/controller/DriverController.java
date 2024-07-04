package com.fiap.parking.meter.controller;

import com.fiap.parking.meter.domain.DriverDto;
import com.fiap.parking.meter.entity.DriverEntity;
import com.fiap.parking.meter.service.DriverService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/driver")
@AllArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping()
    public ResponseEntity<DriverEntity> createDriver(@RequestBody DriverDto driverDto) {
        DriverEntity createDriver = driverService.createDriver(driverDto);
        return new ResponseEntity<>(createDriver, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<DriverEntity>> getDriver(@PathVariable("id") String id) {
        Optional<DriverEntity> driver = this.driverService.getById(id);
        return ResponseEntity.ok().body(driver);
    }

    @GetMapping()
    public ResponseEntity<List<DriverEntity>> getAllDrivers() {
        List<DriverEntity> drivers = this.driverService.getAllDrivers();
        return ResponseEntity.ok().body(drivers);
    }

}
