package com.fiap.parking.meter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "vehicle")
public class VehicleEntity {

    @Id
    private String id;

    private String licensePlate;

    private String model;

    private String color;

    @DBRef
    private DriverEntity driver;

}
