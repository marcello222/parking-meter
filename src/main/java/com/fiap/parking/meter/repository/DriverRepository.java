package com.fiap.parking.meter.repository;


import com.fiap.parking.meter.entity.DriverEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DriverRepository extends MongoRepository<DriverEntity, String> {

    boolean existsByEmail(String email);
}
