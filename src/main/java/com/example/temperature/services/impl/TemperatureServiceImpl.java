package com.example.temperature.services.impl;

import com.example.temperature.dto.OpenWeatherMapDto;
import com.example.temperature.dto.WeatherApiDto;
import com.example.temperature.dto.WeatherBitDto;
import com.example.temperature.entities.TemperatureEntity;
import com.example.temperature.exceptions.IncorrectResponseException;
import com.example.temperature.repositories.TemperatureRepository;
import com.example.temperature.services.TemperatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Сервис для работы с температурой.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TemperatureServiceImpl implements TemperatureService {
    private final TemperatureRepository temperatureRepository;
    private final RestTemplate restTemplate;

    /**
     * запрашивает текущую температуру в городе
     *
     * @param location    город и страна
     * @param source      ключ и урл сервиса
     * @param serviceName имя сервиса
     * @return значение температуры
     */
    @Override
    public Double identifyTemperature(
            Map.Entry<String, String> location,
            Map<String, String> source,
            String serviceName
    ) throws HttpClientErrorException, IncorrectResponseException {
        log.debug(
                "Запрос температуры в городе {} из сервиса {}",
                location.getKey(),
                serviceName
        );
        Double temperature = requestTemperature(location, source, serviceName);
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
        log.info(
                "Сохраняем данные для города {} в бд",
                entity.getCity()
        );
        temperatureRepository.save(entity);
        log.info(
                "Успешное сохранение данных для города {} в бд",
                entity.getCity()
        );
    }

    /**
     * Поиск сохраненных температур по локации и дате
     *
     * @param location локация поиска
     * @param date     дата поиска
     * @return результат поиска
     */
    @Override
    public String getTemperature(String location, LocalDate date) {
        if (date == null) return getTemperature(location);
        log.info(
                "Запрос поиска температуры для локации {} и даты {}",
                location,
                date
        );

        Collection<TemperatureEntity> entities = temperatureRepository.findAllByTimeCreateBetweenAndCityOrCountry(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay(),
                location,
                location
        );

        if (entities.isEmpty()) {
            log.warn(
                    "Данных по локации {} и дате {} не найдены.",
                    location,
                    date
            );
            return "Данных по указанной локации и дате не найдены.";
        } else {
            log.debug(
                    "Количество найденных записей для локации {} и даты {} равно {}",
                    location,
                    date,
                    entities.size()
            );
            StringBuffer sb = new StringBuffer();
            entities.forEach(entity -> sb.append(entity.getTemperature()).append(" | ").append(entity.getTimeCreate())
                    .append("\n"));
            return sb.toString();
        }
    }

    /**
     * Поиск сохраненных температур по локации
     *
     * @param location локация поиска
     * @return результат поиска
     */
    @Override
    public String getTemperature(String location) {
        log.info(
                "Запрос поиска последней сохраненной температуры для локации {}",
                location
        );
        Optional<TemperatureEntity> optional = temperatureRepository
                .findDistinctFirstByCityOrCountryOrderByTimeCreateDesc(location, location);

        if (optional.isPresent()) {
            log.debug(
                    "Найдена запись по локации {}",
                    location
            );
            TemperatureEntity entity = optional.get();
            return entity.getTemperature() + " | " + entity.getTimeCreate();
        } else {
            log.warn(
                    "Данных по локации {} не найдены.",
                    location
            );
            return "Данных по указанной локации не найдено.";
        }
    }

    /**
     * Получение температуры в указанном городе из указанного сервиса
     *
     * @param location     город и страна
     * @param source       ключ и урл
     * @param serviceName  имя сервиса
     * @return температура
     */
    private Double requestTemperature(
            Map.Entry<String, String> location,
            Map<String, String> source,
            String serviceName
    ) throws HttpClientErrorException, IncorrectResponseException {
        switch (serviceName) {
            case "openweathermap": {
                String url = UriComponentsBuilder.fromHttpUrl(source.get("url"))
                        .queryParam("appid", source.get("key"))
                        .queryParam("q", location.getKey())
                        .queryParam("units", "metric")
                        .encode().toUriString();

                log.debug(
                        "Сформированный URL для запроса температуры в локации {}: {}",
                        location,
                        url
                );
                OpenWeatherMapDto dto = restTemplate.getForObject(url, OpenWeatherMapDto.class);
                if (dto != null) {
                    return dto.getTemperature();
                }
                else {
                    log.error(
                            "При запросе к сервису {} был получен не корректный результат.",
                            serviceName
                    );
                    throw new IncorrectResponseException("В ответ на запрос получен не корректный результат.");
                }
            }
            case "weatherapi": {
                String url = UriComponentsBuilder.fromHttpUrl(source.get("url"))
                        .queryParam("key", source.get("key"))
                        .queryParam("q", location.getKey())
                        .encode().toUriString();

                log.debug(
                        "Сформированный URL для запроса температуры в локации {}: {}",
                        location,
                        url
                );
                WeatherApiDto dto = restTemplate.getForObject(url, WeatherApiDto.class);
                if (dto != null) {
                    return dto.getTemperature();
                } else {
                    log.error(
                            "При запросе к сервису {} был получен не корректный результат.",
                            serviceName
                    );
                    throw new IncorrectResponseException("В ответ на запрос получен не корректный результат.");
                }
            }
            case "weatherbit": {
                String url = UriComponentsBuilder.fromHttpUrl(source.get("url"))
                        .queryParam("key", source.get("key"))
                        .queryParam("city", location.getKey())
                        .encode().toUriString();

                log.debug(
                        "Сформированный URL для запроса температуры в локации {}: {}",
                        location,
                        url
                );

                WeatherBitDto dto = restTemplate.getForObject(url, WeatherBitDto.class);
                if (dto != null) {
                    return dto.getTemperature();
                } else {
                    log.error(
                            "При запросе к сервису {} был получен не корректный результат.",
                            serviceName
                    );
                    throw new IncorrectResponseException("В ответ на запрос получен не корректный результат.");
                }
            }
            default:
                throw new IllegalStateException("Unexpected value: " + serviceName);
        }
    }
}
