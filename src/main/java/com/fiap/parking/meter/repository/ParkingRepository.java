package com.fiap.parking.meter.repository;


import com.fiap.parking.meter.entity.ParkingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ParkingRepository extends MongoRepository<ParkingEntity, String> {

    @Query(value = "{ 'endDate' : { $gte: { $subtract: [?#{new Date()}, 10 * 60 * 1000] }, $lte: ?#{new Date()} } }")
    List<ParkingEntity> findParkingLotsExpiringWithinRange();

    @Query("{ 'endDate' : { $lte: ?0 } }")
    List<ParkingEntity> findExpired(LocalDateTime currentTime);
}
