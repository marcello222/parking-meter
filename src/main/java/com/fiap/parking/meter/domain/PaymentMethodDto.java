package com.fiap.parking.meter.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodDto {

    private String description;

    private int paymentMethod;

    private String driverId;

    private String parkingId;

}
