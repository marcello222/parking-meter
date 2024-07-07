package com.fiap.parking.meter.repository;


import com.fiap.parking.meter.entity.ParkingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParkingRepository extends MongoRepository<ParkingEntity, String> {

}
