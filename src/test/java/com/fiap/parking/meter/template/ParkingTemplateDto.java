package com.fiap.parking.meter.template;

import com.fiap.parking.meter.domain.ParkingDto;

public class ParkingTemplateDto {

    public static ParkingDto parkingTemplate() {
        return ParkingDto.builder()
                .parkingDuration(2)
                .parkingTypeCode(1)
                .build();
    }
}
