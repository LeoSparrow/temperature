package com.example.temperature.services.impl;

import com.example.temperature.configuration.WeatherProperties;
import com.example.temperature.entities.TemperatureEntity;
import com.example.temperature.services.TemperatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Сервис событий
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl {
    private final WeatherProperties properties;
    private final TemperatureService updateService;

    /**
     * Задача запускающаяся по таймеру.
     * Вызывает методы запроса температуры и сохранения их в базу.
     */
    @Scheduled(fixedDelay = 500000)
    public void updateTemperature() {
        log.debug("Запуск выполнения задачи для обновления данных по температуре.");
        Map<String, String> locations = properties.getLocations();
        Map<String, Map<String, String>> sources = properties.getSources();
        double temperature = 0;

        for (var location : locations.entrySet()) {
            for (var source : sources.entrySet()) {
                temperature += updateService.identifyTemperature(location, source.getValue(), source.getKey());
            }

            BigDecimal big = new BigDecimal(temperature / locations.size());
            double avgTemperature = big.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            log.debug(
                    "Средняя температура для города {} равна {}",
                    location.getKey(),
                    avgTemperature
            );

            TemperatureEntity entity = new TemperatureEntity();
            entity.setCity(location.getKey());
            entity.setCountry(location.getValue());
            entity.setTemperature(avgTemperature);
            entity.setTimeCreate(LocalDateTime.now());

            updateService.insert(entity);
        }

        log.debug("Конец выполнение задачи.");
    }
}
