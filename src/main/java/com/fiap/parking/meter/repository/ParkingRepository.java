package com.fiap.parking.meter.repository;


import com.fiap.parking.meter.entity.ParkingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ParkingRepository extends MongoRepository<ParkingEntity, String> {

    List<ParkingEntity> findAllByEndDateIsNullAndStartDateLessThanEqual(LocalDateTime startDate);

    @Query("{ 'expirationTime' : { $lte: ?0 } }")
    List<ParkingEntity> findExpiringSoon(LocalDateTime cutoffTime);
}
