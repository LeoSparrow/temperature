package com.example.temperature.controllers;

import com.example.temperature.services.TemperatureService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TemperatureController.class)
class TemperatureControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TemperatureService temperatureService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getTemperatureWithCorrectUrlShouldBeReturnResultOK() throws Exception {
        when(temperatureService.getTemperature("location", LocalDate.now())).thenReturn("");
        mockMvc.perform(MockMvcRequestBuilders
                        .get("https://localhost:8888/api/temperature")
                        .queryParam("location", String.valueOf(LocalDate.now())))
                .andExpect(status().isOk());
    }

    @Test
    void getTemperatureWithIncorrectUrlShouldBeReturnResultNotFound() throws Exception {
        when(temperatureService.getTemperature("location", LocalDate.now())).thenReturn("");
        mockMvc.perform(MockMvcRequestBuilders
                        .get("https://localhost:8888/api/temperature/get")
                        .queryParam("location", String.valueOf(LocalDate.now())))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTemperatureWithCorrectUrlShouldBeReturnCorrectStringResult() throws Exception {
        String expectedResult = "Данных по указанной локации и дате не найдены.";
        when(temperatureService.getTemperature(Mockito.any(), Mockito.any())).thenReturn(expectedResult);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("https://localhost:8888/api/temperature")
                        .queryParam("location", "Moscow")
                        .queryParam("date", String.valueOf(LocalDate.now())))
                .andExpect(status().isOk())
                .andReturn();

        String actualResult = mvcResult.getResponse().getContentAsString();

        Assertions.assertEquals(expectedResult, actualResult);
    }
}