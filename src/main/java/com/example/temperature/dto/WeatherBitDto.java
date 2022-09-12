package com.example.temperature.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * ДТО для сервиса www.weatherbit.io
 */
@Data
public class WeatherBitDto {
    private String city;
    private double temperature;

    /**
     * Метод распаковки вложенного массива data.
     * После распаковки сохраняет значения Город и Температура.
     */
    @JsonProperty("data")
    private void unpackData(List<Map<String, Object>> list) {
        if (!list.isEmpty()) {
            this.city = (String) list.get(0).get("city_name");
            this.temperature = (double) list.get(0).get("temp");
        }
    }
}
