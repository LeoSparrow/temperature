package com.example.temperature.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class TemperatureDto {
    private String city;
    private String country;
    private double temperature;
    private Instant timeCreate;
}
