package com.fiap.parking.meter.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.parking.meter.domain.DriverDto;
import com.fiap.parking.meter.domain.PaymentMethodDto;
import com.fiap.parking.meter.domain.VehicleDto;
import com.fiap.parking.meter.repository.DriverRepository;
import com.fiap.parking.meter.repository.PaymentMethodRepository;
import com.fiap.parking.meter.template.DriverTemplateDto;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentMethodIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @BeforeEach
    public void clearDatabase() {
        driverRepository.deleteAll();
        paymentMethodRepository.deleteAll();
    }

    @Test
    public void testShouldCreatePaymentMethod() throws Exception {
        DriverDto driverDto = DriverTemplateDto.DriverTemplate();

        MvcResult result = mockMvc.perform(post("/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String createdDriverId = JsonPath.parse(response).read("$.id");

        PaymentMethodDto paymentMethodDto = PaymentMethodTemplateDto.paymentMethodTemplate();
        paymentMethodDto.setDriverId(createdDriverId);

        mockMvc.perform(post("/payment-method" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentMethodDto)))
                .andExpect(status().isCreated());

        assertEquals(1, paymentMethodRepository.findAll().size());

    }

    @Test
    public void testShouldNotCreate_with_InvalidPaymentMethod() throws Exception {
        DriverDto driverDto = DriverTemplateDto.DriverTemplate();

        MvcResult result = mockMvc.perform(post("/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String createdDriverId = JsonPath.parse(response).read("$.id");

        PaymentMethodDto paymentMethodDto = PaymentMethodTemplateDto.paymentMethodTemplate();
        paymentMethodDto.setDriverId(createdDriverId);
        paymentMethodDto.setPaymentMethod(4);

        // Solicitação inválida metodo de pagamento invalido
        mockMvc.perform(post("/payment-method")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentMethodDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid PaymentMethod value: " + paymentMethodDto.getPaymentMethod()));
    }


}
