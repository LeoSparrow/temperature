package com.example.temperature.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties("weather")
public class WeatherProperties {
    private Map<String, Map<String, String>> sources;
    private Map<String, String> locations;
}
