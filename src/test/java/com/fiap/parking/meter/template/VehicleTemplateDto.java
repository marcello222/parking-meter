package com.fiap.parking.meter.template;

import com.fiap.parking.meter.domain.VehicleDto;
import com.fiap.parking.meter.entity.VehicleEntity;

import java.util.Arrays;
import java.util.List;


public class VehicleTemplateDto {

    public static VehicleDto VehicleTemplate() {
        return VehicleDto.builder()
                .licensePlate("ABC-1234")
                .model("Uno")
                .driverId(null)
                .build();
    }


    public static List<VehicleDto> TwoVehiclesTemplate() {
        VehicleDto vehicle1 = VehicleDto.builder()
                .licensePlate("aaaaa")
                .model("Uno")
                .build();

        VehicleDto vehicle2 = VehicleDto.builder()
                .licensePlate("dddd")
                .model("Palio")
                .build();

        return Arrays.asList(vehicle1, vehicle2);
    }
}
