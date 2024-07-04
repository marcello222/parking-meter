package com.fiap.parking.meter.template;

import com.fiap.parking.meter.domain.DriverDto;

public class DriverTemplateDto {

    public static DriverDto DriverTemplate() {
        return DriverDto.builder()
                .name("Joao")
                .email("Test Email")
                .phone("123444")
                .address("Street Test, 123")
                .build();
    }
}
