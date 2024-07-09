package com.fiap.parking.meter.service.impl;

import com.fiap.parking.meter.domain.ParkingDto;
import com.fiap.parking.meter.domain.PaymentMethodDto;
import com.fiap.parking.meter.entity.DriverEntity;
import com.fiap.parking.meter.entity.ParkingEntity;
import com.fiap.parking.meter.entity.PaymentMethodEntity;
import com.fiap.parking.meter.entity.VehicleEntity;
import com.fiap.parking.meter.enums.ParkingPeriodType;
import com.fiap.parking.meter.enums.PaymentMethodType;
import com.fiap.parking.meter.exception.DriverNotFoundException;
import com.fiap.parking.meter.exception.PaymentMethodNotFoundException;
import com.fiap.parking.meter.exception.VehicleNotAssociatedWithDriverException;
import com.fiap.parking.meter.mapper.ParkingMapper;
import com.fiap.parking.meter.repository.DriverRepository;
import com.fiap.parking.meter.repository.ParkingRepository;
import com.fiap.parking.meter.repository.PaymentMethodRepository;
import com.fiap.parking.meter.repository.VehicleRepository;
import com.fiap.parking.meter.service.ParkingService;
import com.fiap.parking.meter.service.PaymentMethodService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ParkingServiceImpl implements ParkingService {

    private final DriverRepository driverRepository;

    private final ParkingRepository parkingRepository;

    private final VehicleRepository vehicleRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final ParkingMapper parkingMapper;

    private final PaymentMethodService paymentMethodService;

    public ParkingServiceImpl(ParkingRepository parkingRepository, DriverRepository driverRepository, VehicleRepository vehicleRepository,
                              ParkingMapper parkingMapper, PaymentMethodRepository paymentMethodRepository, PaymentMethodService paymentMethodService) {
        this.driverRepository = driverRepository;
        this.parkingRepository = parkingRepository;
        this.vehicleRepository = vehicleRepository;
        this.parkingMapper = parkingMapper;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodService = paymentMethodService;
    }


    @Override
    public ParkingEntity createParking(ParkingDto parkingDto) {
        DriverEntity driver = driverRepository.findById(parkingDto.getDriverId()).orElseThrow(DriverNotFoundException::new);

        if (vehicleRepository.findById(parkingDto.getVehicleId()).isEmpty()) {
            throw new DataIntegrityViolationException("The vehicle is not associated with the driver = " + parkingDto.getVehicleId());
        }

        VehicleEntity vehicle = vehicleRepository.findById(parkingDto.getVehicleId()).orElseThrow(VehicleNotAssociatedWithDriverException::new);

        ParkingEntity parking = parkingMapper.toEntity(parkingDto);

        parking.setDriver(driver);
        parking.setVehicle(vehicle);

        PaymentMethodEntity updatedOrFoundPaymentMethod = handlePaymentMethodUpdateOrCreate(parkingDto);
        parking.setPaymentMethod(updatedOrFoundPaymentMethod);

        parking = parkingRepository.save(parking);

        return parking;
    }

    @Override
    public ParkingEntity getParking(String id) {
        return parkingRepository.findById(id).orElseThrow();
    }


    private PaymentMethodEntity handlePaymentMethodUpdateOrCreate(ParkingDto parkingDto) throws PaymentMethodNotFoundException {
        if (parkingDto.getParkingTypeCode() == ParkingPeriodType.FIXED_PERIOD.getValue()) {
            PaymentMethodEntity existingPaymentMethod = paymentMethodRepository.findById(parkingDto.getPaymentMethodId())
                    .orElseThrow(PaymentMethodNotFoundException::new);

            PaymentMethodDto paymentMethodDto = new PaymentMethodDto();
            paymentMethodDto.setPaymentMethod(PaymentMethodType.PIX.getValue());

            existingPaymentMethod.setPaymentMethod(paymentMethodDto.getPaymentMethod());

            return paymentMethodService.updatePaymentMethod(parkingDto.getPaymentMethodId().toString(), paymentMethodDto);
        } else {
            return paymentMethodRepository.findById(parkingDto.getPaymentMethodId())
                    .orElseThrow(PaymentMethodNotFoundException::new);
        }

    }
}
