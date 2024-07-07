package com.fiap.parking.meter.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.parking.meter.domain.DriverDto;
import com.fiap.parking.meter.domain.VehicleDto;
import com.fiap.parking.meter.repository.DriverRepository;
import com.fiap.parking.meter.repository.VehicleRepository;
import com.fiap.parking.meter.template.DriverTemplateDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VehicleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @BeforeEach
    public void clearDatabase() {
        vehicleRepository.deleteAll();
        driverRepository.deleteAll();
    }

    @Test
    public void testCreateVehicleWithExistingDriver() throws Exception {
        DriverDto driverRequest = DriverTemplateDto.driverTemplate();

        MvcResult driverResult = mockMvc.perform(post("/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String driverResponse = driverResult.getResponse().getContentAsString();
        String createdDriverId = JsonPath.parse(driverResponse).read("$.id");

        VehicleDto vehicleRequest = VehicleTemplateDto.vehicleTemplate();
        vehicleRequest.setDriverId(createdDriverId);

        MvcResult vehicleResult = mockMvc.perform(post("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String vehicle = vehicleResult.getResponse().getContentAsString();
        VehicleDto vehicleResponse = objectMapper.readValue(vehicle, VehicleDto.class);

        assertEquals(vehicleRequest.getLicensePlate(), vehicleResponse.getLicensePlate());
        assertEquals(vehicleRequest.getModel(), vehicleResponse.getModel());
    }
}
