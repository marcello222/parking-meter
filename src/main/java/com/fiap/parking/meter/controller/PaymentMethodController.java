package com.fiap.parking.meter.controller;

import com.fiap.parking.meter.domain.PaymentMethodDto;
import com.fiap.parking.meter.entity.ParkingEntity;
import com.fiap.parking.meter.entity.PaymentMethodEntity;
import com.fiap.parking.meter.service.PaymentMethodService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment-method")
@AllArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @PostMapping
    public ResponseEntity<PaymentMethodEntity> createdPaymentMethod(@RequestBody PaymentMethodDto paymentMethodDto) {
        return new ResponseEntity<>(paymentMethodService.createPaymentMethod(paymentMethodDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodEntity> updatePaymentMethod(@PathVariable("id") String id, @RequestBody PaymentMethodDto paymentMethodDto) {
        return new ResponseEntity<>(paymentMethodService.updatePaymentMethod(id, paymentMethodDto), HttpStatus.OK);
    }


}
