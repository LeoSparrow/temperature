package com.example.temperature.services;

import com.example.temperature.entities.TemperatureEntity;

import java.time.LocalDate;
import java.util.Map;

public interface TemperatureService {
    Double identifyTemperature(Map.Entry<String, String> location, Map<String, String> source, String serviceName);

    void insert(TemperatureEntity entity);

    String getTemperature(String location, LocalDate date);

    String getTemperature(String location);
}
