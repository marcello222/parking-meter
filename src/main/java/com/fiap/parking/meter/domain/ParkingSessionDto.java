package com.fiap.parking.meter.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSessionDto {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String driverId;

    private String vehicleId;

    private Integer parkingTypeCode;


}
