package com.example.temperature.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * ДТО для сервиса openweathermap.org
 */
@Data
public class OpenWeatherMapDto {
    @JsonProperty("name")
    private String city;
    private double temperature;

    /**
     * Метод распаковки вложенного массива main.
     * После распаковки сохраняет значение Температура.
     */
    @JsonProperty("main")
    private void unpackMain(Map<String, Object> map) {
        this.temperature = (double) map.get("temp");
    }
}
