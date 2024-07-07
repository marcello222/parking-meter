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
public class ParkingDto {

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer parkingTypeCode;

    private Double value;

    private String vehicleId;

    private String paymentMethodId;

    private String driverId;
}
