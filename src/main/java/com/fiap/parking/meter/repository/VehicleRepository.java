package com.fiap.parking.meter.repository;


import com.fiap.parking.meter.entity.VehicleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VehicleRepository extends MongoRepository<VehicleEntity, String> {


}
