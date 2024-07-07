package com.fiap.parking.meter.repository;

import com.fiap.parking.meter.entity.PaymentMethodEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentMethodRepository extends MongoRepository<PaymentMethodEntity, String> {
}
