package com.fiap.parking.meter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "parking")
public class ParkingEntity {

    @Id
    private String id;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer parkingTypeCode;

    private Double value;

    @DBRef
    private VehicleEntity vehicle;

    @DBRef
    private PaymentMethodEntity paymentMethod;

    @DBRef
    private DriverEntity driver;
}
