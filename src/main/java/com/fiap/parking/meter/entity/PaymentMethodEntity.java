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
@Document(collection = "payment-method")
public class PaymentMethodEntity {

    @Id
    private String id;

    private String description;

    private Integer paymentMethod;

    @DBRef
    private DriverEntity driver;

}
