package com.fiap.parking.meter.template;

import com.fiap.parking.meter.domain.ParkingDto;

import java.time.LocalDateTime;

public class ParkingTemplateDto {

    public static ParkingDto parkingTemplate() {
        return ParkingDto.builder()
                .startDate(LocalDateTime.parse("2024-07-07T10:00:00"))
                .endDate(LocalDateTime.parse("2024-07-10T11:00:00"))
                .value(10.0)
                .parkingTypeCode(2)
                .build();


    }

}
