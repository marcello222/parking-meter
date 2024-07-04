package com.fiap.parking.meter.service.impl;

import com.fiap.parking.meter.domain.DriverDto;
import com.fiap.parking.meter.entity.DriverEntity;
import com.fiap.parking.meter.mapper.DriverMapper;
import com.fiap.parking.meter.repository.DriverRepository;
import com.fiap.parking.meter.service.DriverService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class DriveServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverMapper driverMapper;

    public DriveServiceImpl(DriverRepository driverRepository, DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    @Override
    public DriverEntity createDriver(DriverDto driverDto) {
        if(driverRepository.existsByEmail(driverDto.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists: " + driverDto.getEmail());
        }
        DriverEntity newDriver = driverMapper.toEntity(driverDto);
        DriverEntity savedDriver = driverRepository.save(newDriver);
        return savedDriver;
    }


    @Override
    public Optional<DriverEntity> getById(String id) {
        return this.driverRepository.findById(id);
    }

    @Override
    public List<DriverEntity> getAllDrivers() {
        return this.driverRepository.findAll();
    }

}
