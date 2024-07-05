package com.fiap.parking.meter.template;

import com.fiap.parking.meter.domain.DriverDto;
import com.fiap.parking.meter.domain.VehicleDto;

import java.util.Collections;

public class DriverTemplateDto {

    public static DriverDto DriverTemplate() {
        return DriverDto.builder()
                .name("Joao")
                .email("Test Email")
                .phone("123444")
                .address("Street Test, 123")
                .build();
    }

    public static DriverDto DriverWithVehiclesTemplate() {
        return DriverDto.builder()
                .name("Joao")
                .email("Test Email")
                .phone("123444")
                .address("Street Test, 123")
                .vehicles(VehicleTemplateDto.TwoVehiclesTemplate())
                .build();
    }
}
