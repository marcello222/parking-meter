package com.fiap.parking.meter.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.parking.meter.domain.DriverDto;
import com.fiap.parking.meter.repository.DriverRepository;
import com.fiap.parking.meter.template.DriverTemplateDto;
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
       DriverDto driverDto = DriverTemplateDto.DriverTemplate();

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
}
