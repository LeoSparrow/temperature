package com.example.temperature.services.impl;

import com.example.temperature.dto.OpenWeatherMapDto;
import com.example.temperature.dto.WeatherApiDto;
import com.example.temperature.dto.WeatherBitDto;
import com.example.temperature.entities.TemperatureEntity;
import com.example.temperature.repositories.TemperatureRepository;
import com.example.temperature.services.TemperatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * Сервис для работы с температурой.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TemperatureServiceImpl implements TemperatureService {
    private final TemperatureRepository repository;

    /**
     * запрашивает текущую температуру в городе
     *
     * @param location    город и страна
     * @param source      ключ и урл сервиса
     * @param serviceName имя сервиса
     * @return значение температуры
     */
    @Override
    public Double identifyTemperature(Map.Entry<String, String> location, Map<String, String> source, String serviceName) {
        RestTemplate restTemplate = new RestTemplate();
        log.debug(
                "Запрос температуры в городе {} и сервиса {}",
                location.getKey(),
                serviceName
        );
        Double temperature = getTemperature(location, source, serviceName, restTemplate);
        log.debug(
                "Сервис {} вернул температуру {} для города {}",
                serviceName,
                temperature,
                location.getKey()
        );

        return temperature;
    }

    /**
     * сохранение данных в бд.
     *
     * @param entity сущность сохраняемая в бд
     */
    @Transactional
    @Override
    public void insert(TemperatureEntity entity) {
        repository.save(entity);
    }

    /**
     * Получение температуры в указанном городе из указанного сервиса
     *
     * @param location     город и страна
     * @param source       ключ и урл
     * @param serviceName  имя сервиса
     * @param restTemplate
     * @return температура
     */
    private Double getTemperature(
            Map.Entry<String, String> location,
            Map<String, String> source,
            String serviceName,
            RestTemplate restTemplate
    ) {
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(source.get("url"));
        switch (serviceName) {
            case "openweathermap": {
                url.queryParam("appid", source.get("key"))
                        .queryParam("q", location.getKey())
                        .queryParam("units", "metric");
                OpenWeatherMapDto weather = restTemplate.getForObject(url.encode().toUriString(), OpenWeatherMapDto.class);
                if (weather != null) return weather.getTemperature();
            }
            case "weatherapi": {
                url.queryParam("key", source.get("key"))
                        .queryParam("q", location.getKey());
                WeatherApiDto weather = restTemplate.getForObject(url.encode().toUriString(), WeatherApiDto.class);
                if (weather != null) return weather.getTemperature();
            }
            case "weatherbit": {
                url.queryParam("key", source.get("key"))
                        .queryParam("city", location.getKey());
                WeatherBitDto weather = restTemplate.getForObject(url.encode().toUriString(), WeatherBitDto.class);
                if (weather != null) return weather.getTemperature();
            }
            default:
                throw new IllegalStateException("Unexpected value: " + serviceName);
        }
    }
}
