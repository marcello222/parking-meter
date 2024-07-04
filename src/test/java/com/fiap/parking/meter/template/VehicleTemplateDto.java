package com.fiap.parking.meter.template;

import com.fiap.parking.meter.domain.VehicleDto;


public class VehicleTemplateDto {

    public static VehicleDto VehicleTemplate() {
        return VehicleDto.builder()
                .licensePlate("ABC-1234")
                .model("Uno")
                .driverId(null)
                .build();
    }
}
