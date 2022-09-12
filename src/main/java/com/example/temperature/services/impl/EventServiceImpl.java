package com.example.temperature.services.impl;

import com.example.temperature.configuration.WeatherProperties;
import com.example.temperature.entities.TemperatureEntity;
import com.example.temperature.exceptions.IncorrectResponseException;
import com.example.temperature.services.TemperatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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
    private final TemperatureService temperatureService;


    /**
     * Задача запускающаяся по таймеру.
     * Вызывает методы запроса температуры и сохранения их в базу.
     */
    @Scheduled(cron = "${cron}")
    public void updateTemperature() {
        log.info("Запуск выполнения задачи для обновления данных по температуре.");
        Map<String, String> locations = properties.getLocations();
        Map<String, Map<String, String>> sources = properties.getSources();
        log.info(
                "Список город: {}",
                locations
        );

        for (Map.Entry<String, String> location : locations.entrySet()) {
            double temperature = 0;
            int successfulCount = sources.size();
            for (Map.Entry<String, Map<String, String>> source : sources.entrySet()) {
                try {
                    temperature += temperatureService.identifyTemperature(location, source.getValue(), source.getKey());
                } catch (HttpClientErrorException | IncorrectResponseException e) {
                    successfulCount--;
                    log.error(
                            "Ошибка при запросе температуры из сервиса {}",
                            source.getKey(),
                            e
                    );
                }
            }

            if (successfulCount == 0) {
                log.warn(
                        "Для локации {} не удалось получить информацию ни из одного источника." +
                                "Запись не будет сохранена в бд",
                        location
                );
            }
            BigDecimal big = new BigDecimal(temperature / successfulCount);
            double avgTemperature = big.setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            log.info(
                    "Средняя температура для города {} равна {}",
                    location.getKey(),
                    avgTemperature
            );

            TemperatureEntity entity = new TemperatureEntity();
            entity.setCity(location.getKey());
            entity.setCountry(location.getValue());
            entity.setTemperature(avgTemperature);
            entity.setTimeCreate(LocalDateTime.now());

            temperatureService.insert(entity);
        }

        log.info("Конец выполнение задачи.");
    }
}
