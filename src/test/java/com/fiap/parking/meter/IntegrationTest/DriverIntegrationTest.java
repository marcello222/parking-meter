package com.fiap.parking.meter.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.parking.meter.domain.DriverDto;
import com.fiap.parking.meter.domain.VehicleDto;
import com.fiap.parking.meter.repository.DriverRepository;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DriverIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    DriverRepository driverRepository;

    @BeforeEach
    public void clearDatabase() {
        driverRepository.deleteAll();
    }

    @Test
    public void testCreateAndGetDriver() throws Exception {
        DriverDto driverDto = DriverTemplateDto.driverTemplate();

        MvcResult result = mockMvc.perform(post("/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String createdDriverId = JsonPath.parse(response).read("$.id");

        result = mockMvc.perform(get("/driver/" + createdDriverId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        response = result.getResponse().getContentAsString();
        DriverDto returnedDriverDto = objectMapper.readValue(response, DriverDto.class);

        assertEquals(driverDto.getName(), returnedDriverDto.getName());
        assertEquals(driverDto.getEmail(), returnedDriverDto.getEmail());
        assertEquals(driverDto.getPhone(), returnedDriverDto.getPhone());
        assertEquals(driverDto.getAddress(), returnedDriverDto.getAddress());
    }

    @Test
    public void testShouldNotCreateDriverWithDuplicateEmail() throws Exception {
        String duplicateEmail = "duplicate" + System.currentTimeMillis() + "@email.com";

        DriverDto driverDto = new DriverDto();
        driverDto.setName("Teste 4");
        driverDto.setEmail(duplicateEmail);
        driverDto.setPhone("teste 2");
        driverDto.setAddress("ALfredo 2");

        // Primeira solicitação POST deve ser bem-sucedida
        mockMvc.perform(post("/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDto)))
                .andExpect(status().isCreated());

        // Segunda solicitação POST com o mesmo e-mail deve resultar em conflito
        mockMvc.perform(post("/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Email already exists: " + duplicateEmail));
    }

    @Test
    public void testUpdateDriver() throws Exception {
        DriverDto driverDto = DriverTemplateDto.driverTemplate();

        // post the driver
        MvcResult result = mockMvc.perform(post("/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String createdDriverId = JsonPath.parse(response).read("$.id");

        // get the driver
        result = mockMvc.perform(get("/driver/" + createdDriverId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        response = result.getResponse().getContentAsString();
        DriverDto returnedDriverDto = objectMapper.readValue(response, DriverDto.class);

        assertEquals(driverDto.getEmail(), returnedDriverDto.getEmail());
        assertEquals(driverDto.getAddress(), returnedDriverDto.getAddress());

        // update the email and address
        driverDto.setEmail("updatedEmail@example.com");
        driverDto.setAddress("updated address");

        mockMvc.perform(put("/driver/" + createdDriverId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDto)))
                .andExpect(status().isOk());

        // get the updated driver
        result = mockMvc.perform(get("/driver/" + createdDriverId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        response = result.getResponse().getContentAsString();
        returnedDriverDto = objectMapper.readValue(response, DriverDto.class);


        // Assert the updated fields
        assertEquals(driverDto.getEmail(), returnedDriverDto.getEmail());
        assertEquals(driverDto.getAddress(), returnedDriverDto.getAddress());
    }

    @Test
    public void testLinkVehicleToDriver() throws Exception {
        // Create a DriverDto with vehicles using the template
        DriverDto driverWithVehicles = DriverTemplateDto.driverTemplate();

        // Save the driver to the database to ensure the ID exists
        MvcResult result = mockMvc.perform(post("/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverWithVehicles)))
                .andExpect(status().isCreated())
                .andReturn();

        // Parse the response to get the created driver's ID
        String response = result.getResponse().getContentAsString();
        String createdDriverId = JsonPath.parse(response).read("$.id");

        // Create the vehicles using the template
        List<VehicleDto> vehicles = VehicleTemplateDto.twoVehiclesTemplate();

        // Perform a POST request to link vehicles to the driver
        result = mockMvc.perform(post("/driver/" + createdDriverId + "/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicles)))
                .andExpect(status().isOk())
                .andReturn();

        // Parse the response
        response = result.getResponse().getContentAsString();
        DriverDto returnedDriverDto = objectMapper.readValue(response, DriverDto.class);

        assertEquals(2, returnedDriverDto.getVehicles().size());
    }

}
