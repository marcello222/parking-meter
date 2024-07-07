package com.fiap.parking.meter.template;

import com.fiap.parking.meter.domain.PaymentMethodDto;

public class PaymentMethodTemplateDto {

    public static PaymentMethodDto paymentMethodTemplate() {
        return PaymentMethodDto.builder()
                .description("Escolhido metodo de pagamento")
                .paymentMethod(1)
                .build();
    }

}
