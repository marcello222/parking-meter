package com.fiap.parking.meter.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverDto {

    private String name;

    private String email;

    private String phone;

    private String address;

    private List<VehicleDto> vehicles;

    public String getEmail() {
        return this.email;
    }
}
