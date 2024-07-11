package com.fiap.parking.meter.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.parking.meter.domain.DriverDto;
import com.fiap.parking.meter.domain.ParkingDto;
import com.fiap.parking.meter.domain.PaymentMethodDto;
import com.fiap.parking.meter.domain.VehicleDto;
import com.fiap.parking.meter.enums.PaymentMethodType;
import com.fiap.parking.meter.repository.DriverRepository;
import com.fiap.parking.meter.repository.ParkingRepository;
import com.fiap.parking.meter.repository.PaymentMethodRepository;
import com.fiap.parking.meter.repository.VehicleRepository;
import com.fiap.parking.meter.template.DriverTemplateDto;
import com.fiap.parking.meter.template.ParkingTemplateDto;
import com.fiap.parking.meter.template.PaymentMethodTemplateDto;
import com.fiap.parking.meter.template.VehicleTemplateDto;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ParkingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    ParkingRepository parkingRepository;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @BeforeEach
    public void clearDatabase() {
        driverRepository.deleteAll();
        paymentMethodRepository.deleteAll();
        parkingRepository.deleteAll();
        vehicleRepository.deleteAll();
    }

    @Test
    public void testShouldCreatedParking() throws Exception {
        DriverDto driverDto = DriverTemplateDto.driverTemplate();

        MvcResult result = mockMvc.perform(post("/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String driverId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        PaymentMethodDto paymentMethodDto = PaymentMethodTemplateDto.paymentMethodTemplate();
        paymentMethodDto.setDriverId(driverId);

        result = mockMvc.perform(post("/payment-method")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentMethodDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String paymentMethodId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        VehicleDto vehicleDto = VehicleTemplateDto.vehicleTemplate();
        vehicleDto.setDriverId(driverId);

        result = mockMvc.perform(post("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String vehicleId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        ParkingDto parkingDto = ParkingTemplateDto.parkingTemplate();
        parkingDto.setDriverId(driverId);
        parkingDto.setPaymentMethodId(paymentMethodId);
        parkingDto.setVehicleId(vehicleId);


        result = mockMvc.perform(post("/parking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parkingDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String parkingId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        assertEquals(1, parkingRepository.count());
        assertEquals(1, driverRepository.count());
        assertEquals(1, paymentMethodRepository.count());
        assertEquals(1, vehicleRepository.count());
    }

    @Test
    public void testShouldCreatedParking_with_ParkingPeriodType_PER_HOUR_with_PaymentMethodType_CREDIT() throws Exception {
        DriverDto driverDto = DriverTemplateDto.driverTemplate();

        MvcResult result = mockMvc.perform(post("/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String driverId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        PaymentMethodDto paymentMethodDto = PaymentMethodTemplateDto.paymentMethodTemplate();
        paymentMethodDto.setDriverId(driverId);

        result = mockMvc.perform(post("/payment-method")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentMethodDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String paymentMethodId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        VehicleDto vehicleDto = VehicleTemplateDto.vehicleTemplate();
        vehicleDto.setDriverId(driverId);

        result = mockMvc.perform(post("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String vehicleId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        ParkingDto parkingDto = ParkingTemplateDto.parkingTemplate();
        parkingDto.setDriverId(driverId);
        parkingDto.setPaymentMethodId(paymentMethodId);
        parkingDto.setVehicleId(vehicleId);

        result = mockMvc.perform(post("/parking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parkingDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String parkingId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");


        MvcResult getParkingResult = mockMvc.perform(get("/parking/" + parkingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(PaymentMethodType.DEBIT_CARD.getValue(), 2);

    }

    @Test
    public void testShouldNotCreatedParking_with_ParkingPeriodType_PER_HOUR_with_PaymentMethodType_PIX() throws Exception {
        DriverDto driverDto = DriverTemplateDto.driverTemplate();

        MvcResult result = mockMvc.perform(post("/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String driverId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        PaymentMethodDto paymentMethodDto = PaymentMethodTemplateDto.paymentMethodTemplate();
        paymentMethodDto.setPaymentMethod(3);
        paymentMethodDto.setDriverId(driverId);

        result = mockMvc.perform(post("/payment-method")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentMethodDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String paymentMethodId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        VehicleDto vehicleDto = VehicleTemplateDto.vehicleTemplate();
        vehicleDto.setDriverId(driverId);

        result = mockMvc.perform(post("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String vehicleId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");

        ParkingDto parkingDto = ParkingTemplateDto.parkingTemplate();
        parkingDto.setDriverId(driverId);
        parkingDto.setPaymentMethodId(paymentMethodId);
        parkingDto.setVehicleId(vehicleId);


       mockMvc.perform(post("/parking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parkingDto)))
                .andExpect(status().isConflict())
               .andExpect(content().string("Payment method PIX is not allowed for hourly parking"));
    }

}
