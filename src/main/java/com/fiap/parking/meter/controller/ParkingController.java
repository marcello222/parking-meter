package com.fiap.parking.meter.controller;

import com.fiap.parking.meter.domain.ParkingDto;
import com.fiap.parking.meter.entity.ParkingEntity;
import com.fiap.parking.meter.service.ParkingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parking")
@AllArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;

    @PostMapping
    public ResponseEntity<ParkingEntity> createParking(@RequestBody ParkingDto parkingDto) {
        ParkingEntity newParking = parkingService.createParking(parkingDto);
        return new ResponseEntity<>(newParking, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingEntity> getParking(@PathVariable String id) {
        ParkingEntity parking = parkingService.getParking(id);
        return new ResponseEntity<>(parking, HttpStatus.OK);
    }


}
