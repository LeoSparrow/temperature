package com.example.temperature.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * ДТО для сервиса www.weatherapi.com
 */
@Data
public class WeatherApiDto {
    private String city;
    private String country;
    private double temperature;

    /**
     * Метод распаковки вложенного массива location.
     * После распаковки сохраняет значения Город и Страна.
     */
    @JsonProperty("location")
    private void unpackLocation(Map<String, Object> map) {
        this.city = (String) map.get("name");
        this.country = (String) map.get("country");
    }

    /**
     * Метод распаковки вложенного массива current.
     * После распаковки сохраняет значение Температура.
     */
    @JsonProperty("current")
    private void unpackCurrent(Map<String, Object> map) {
        this.temperature = (double) map.get("temp_c");
    }
}
