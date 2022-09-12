package com.example.temperature.services.impl;

import com.example.temperature.dto.OpenWeatherMapDto;
import com.example.temperature.dto.WeatherApiDto;
import com.example.temperature.dto.WeatherBitDto;
import com.example.temperature.entities.TemperatureEntity;
import com.example.temperature.exceptions.IncorrectResponseException;
import com.example.temperature.repositories.TemperatureRepository;
import com.example.temperature.services.TemperatureService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

class TemperatureServiceImplTest {

    private TemperatureService temperatureService;
    @Mock
    private TemperatureRepository temperatureRepository;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        temperatureRepository = Mockito.mock(TemperatureRepository.class);
        temperatureService = new TemperatureServiceImpl(temperatureRepository, restTemplate);
    }

    @Test
    void identifyTemperatureShouldBeCorrectResultWithOpenWeatherMap() {
        String url = "https://api.openweathermap.org/data/2.5/weather?appid=1d7659b294d1085934bf0a736e0d35b3&q=Izhevsk&units=metric";
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>("Izhevsk", "Russia");
        Map<String, String> sources = new HashMap<>();
        sources.put("key", "1d7659b294d1085934bf0a736e0d35b3");
        sources.put("url", "https://api.openweathermap.org/data/2.5/weather");
        String serviceName = "openweathermap";
        OpenWeatherMapDto dto = new OpenWeatherMapDto();
        dto.setCity("city");
        dto.setTemperature(10);

        when(restTemplate.getForObject(url, OpenWeatherMapDto.class)).thenReturn(dto);
        double actualTemperature = temperatureService.identifyTemperature(
                entry,
                sources,
                serviceName
        );
        double expectedTemperature = dto.getTemperature();

        Assertions.assertEquals(actualTemperature, expectedTemperature);
    }

    @Test
    void identifyTemperatureShouldBeIncorrectResultWithOpenWeatherMap() {
        String url = "https://api.openweathermap.org/data/2.5/weather?appid=1d7659b294d1085934bf0a736e0d35b3&q=Izhevsk&units=metric";
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>("Izhevsk", "Russia");
        Map<String, String> sources = new HashMap<>();
        sources.put("key", "1d7659b294d1085934bf0a736e0d35b3");
        sources.put("url", "https://api.openweathermap.org/data/2.5/weather");
        String serviceName = "openweathermap";
        OpenWeatherMapDto dto = new OpenWeatherMapDto();
        dto.setCity("city");
        dto.setTemperature(10);

        when(restTemplate.getForObject(url, OpenWeatherMapDto.class)).thenReturn(dto);
        double actualTemperature = temperatureService.identifyTemperature(
                entry,
                sources,
                serviceName
        );
        double expectedTemperature = 20;

        Assertions.assertNotEquals(actualTemperature, expectedTemperature);
    }

    @Test
    void identifyTemperatureShouldBeCorrectResultWithWeatherApi() {
        String url = "http://api.weatherapi.com/v1/current.json?key=29d973a0d4f943799c064943221109&q=Izhevsk";
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>("Izhevsk", "Russia");
        Map<String, String> sources = new HashMap<>();
        sources.put("key", "29d973a0d4f943799c064943221109");
        sources.put("url", "http://api.weatherapi.com/v1/current.json");
        String serviceName = "weatherapi";
        WeatherApiDto dto = new WeatherApiDto();
        dto.setCity("city");
        dto.setTemperature(10);

        when(restTemplate.getForObject(url, WeatherApiDto.class)).thenReturn(dto);
        double actualTemperature = temperatureService.identifyTemperature(
                entry,
                sources,
                serviceName
        );
        double expectedTemperature = dto.getTemperature();

        Assertions.assertEquals(actualTemperature, expectedTemperature);
    }

    @Test
    void identifyTemperatureShouldBeIncorrectResultWithWeatherApi() {
        String url = "http://api.weatherapi.com/v1/current.json?key=29d973a0d4f943799c064943221109&q=Izhevsk";
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>("Izhevsk", "Russia");
        Map<String, String> sources = new HashMap<>();
        sources.put("key", "29d973a0d4f943799c064943221109");
        sources.put("url", "http://api.weatherapi.com/v1/current.json");
        String serviceName = "weatherapi";
        WeatherApiDto dto = new WeatherApiDto();
        dto.setCity("city");
        dto.setTemperature(10);

        when(restTemplate.getForObject(url, WeatherApiDto.class)).thenReturn(dto);
        double actualTemperature = temperatureService.identifyTemperature(
                entry,
                sources,
                serviceName
        );
        double expectedTemperature = 20;

        Assertions.assertNotEquals(actualTemperature, expectedTemperature);
    }

    @Test
    void identifyTemperatureShouldBeCorrectResultWithWeatherBit() {
        String url = "https://api.weatherbit.io/v2.0/current?key=30a30e0e924248d4b200e5fe84b7047f&city=Izhevsk";
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>("Izhevsk", "Russia");
        Map<String, String> sources = new HashMap<>();
        sources.put("key", "30a30e0e924248d4b200e5fe84b7047f");
        sources.put("url", "https://api.weatherbit.io/v2.0/current");
        String serviceName = "weatherbit";
        WeatherBitDto dto = new WeatherBitDto();
        dto.setCity("Izhevsk");
        dto.setTemperature(10);

        when(restTemplate.getForObject(url, WeatherBitDto.class)).thenReturn(dto);
        double actualTemperature = temperatureService.identifyTemperature(
                entry,
                sources,
                serviceName
        );
        double expectedTemperature = dto.getTemperature();

        Assertions.assertEquals(actualTemperature, expectedTemperature);
    }

    @Test
    void identifyTemperatureShouldBeIncorrectResultWithWeatherBit() {
        String url = "https://api.weatherbit.io/v2.0/current?key=30a30e0e924248d4b200e5fe84b7047f&city=Izhevsk";
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>("Izhevsk", "Russia");
        Map<String, String> sources = new HashMap<>();
        sources.put("key", "30a30e0e924248d4b200e5fe84b7047f");
        sources.put("url", "https://api.weatherbit.io/v2.0/current");
        String serviceName = "weatherbit";
        WeatherBitDto dto = new WeatherBitDto();
        dto.setCity("city");
        dto.setTemperature(10);

        when(restTemplate.getForObject(url, WeatherBitDto.class)).thenReturn(dto);
        double actualTemperature = temperatureService.identifyTemperature(
                entry,
                sources,
                serviceName
        );
        double expectedTemperature = 20;

        Assertions.assertNotEquals(actualTemperature, expectedTemperature);
    }

    @Test
    void identifyTemperatureShouldBeThrowIncorrectResponseExceptionWithOpenWeatherMapIncorrectURL() {
        String url = "https://api.openweathermap.org/data/2.5/weather?appid=1d7659b294d1085934bf0a736e0d35b3&q=Izhevsk&units=metric";
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>("Izhevsk", "Russia");
        Map<String, String> sources = new HashMap<>();
        sources.put("key", "1d7659b294d1085934bf0a736e0d35b3");
        sources.put("url", "https://api.openweathermap.org/data/2.5");
        String serviceName = "openweathermap";
        OpenWeatherMapDto dto = new OpenWeatherMapDto();
        dto.setCity("city");
        dto.setTemperature(10);

        when(restTemplate.getForObject(url, OpenWeatherMapDto.class)).thenReturn(dto);

        Assertions.assertThrows(IncorrectResponseException.class, () -> temperatureService.identifyTemperature(
                entry,
                sources,
                serviceName
        ));
    }

    @Test
    void identifyTemperatureShouldBeThrowIncorrectResponseExceptionWithWeatherApiIncorrectURL() {
        String url = "http://api.weatherapi.com/v1/current.json?key=29d973a0d4f943799c064943221109&q=Izhevsk";
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>("Izhevsk", "Russia");
        Map<String, String> sources = new HashMap<>();
        sources.put("key", "29d973a0d4f943799c064943221109");
        sources.put("url", "http://api.weatherapi.com/v1/");
        String serviceName = "weatherapi";
        WeatherApiDto dto = new WeatherApiDto();
        dto.setCity("city");
        dto.setTemperature(10);

        when(restTemplate.getForObject(url, WeatherApiDto.class)).thenReturn(dto);

        Assertions.assertThrows(IncorrectResponseException.class, () -> temperatureService.identifyTemperature(
                entry,
                sources,
                serviceName
        ));
    }

    @Test
    void identifyTemperatureShouldBeThrowIncorrectResponseExceptionWithWeatherBitIncorrectURL() {
        String url = "https://api.weatherbit.io/v2.0/current?city=Izhevsk&key=30a30e0e924248d4b200e5fe84b7047f";
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>("Izhevsk", "Russia");
        Map<String, String> sources = new HashMap<>();
        sources.put("key", "30a30e0e924248d4b200e5fe84b7047f");
        sources.put("url", "https://api.weatherbit.io/v2.0");
        String serviceName = "weatherbit";
        WeatherBitDto dto = new WeatherBitDto();
        dto.setCity("Izhevsk");
        dto.setTemperature(10);

        when(restTemplate.getForObject(url, WeatherBitDto.class)).thenReturn(dto);

        Assertions.assertThrows(IncorrectResponseException.class, () -> temperatureService.identifyTemperature(
                entry,
                sources,
                serviceName
        ));
    }

    @Test
    void identifyTemperatureShouldBeThrowIllegalStateExceptionWithIncorrectServiceName() {
        String url = "https://api.weatherbit.io/v2.0/current?city=Izhevsk&key=30a30e0e924248d4b200e5fe84b7047f";
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<>("Izhevsk", "Russia");
        Map<String, String> sources = new HashMap<>();
        sources.put("key", "30a30e0e924248d4b200e5fe84b7047f");
        sources.put("url", "https://api.weatherbit.io/v2.0");
        String serviceName = "serviceName";
        WeatherBitDto dto = new WeatherBitDto();
        dto.setCity("Izhevsk");
        dto.setTemperature(10);

        when(restTemplate.getForObject(url, WeatherBitDto.class)).thenReturn(dto);

        Assertions.assertThrows(IllegalStateException.class, () -> temperatureService.identifyTemperature(
                entry,
                sources,
                serviceName
        ));
    }

    @Test
    void getTemperatureWithIncorrectLocationAndNullDateShouldBeNotResult() {
        String location = "location";
        LocalDate date = null;
        String actualResult = temperatureService.getTemperature(location, date);
        String expectedResult = "Данных по указанной локации не найдено.";

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void getTemperatureWithIncorrectLocationAndCorrectDateShouldBeNotResult() {
        String location = "location";
        LocalDate date = LocalDate.now();
        String actualResult = temperatureService.getTemperature(location, date);
        String expectedResult = "Данных по указанной локации и дате не найдены.";

        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void getTemperatureWithCorrectLocationAndCorrectDateShouldBeCorrectResult() {
        String location = "location";
        LocalDateTime date = LocalDateTime.now();
        TemperatureEntity entity = new TemperatureEntity();
        entity.setId(1L);
        entity.setCity("location");
        entity.setTemperature(36.6);
        entity.setTimeCreate(date);

        when(temperatureRepository.findAllByTimeCreateBetweenAndCityOrCountry(
                date.toLocalDate().atStartOfDay(),
                date.plusDays(1).toLocalDate().atStartOfDay(),
                location,
                location
        )).thenReturn(List.of(entity));
        String actualResult = temperatureService.getTemperature(location, date.toLocalDate());

        String expectedResult = "36.6 | " + date + "\n";
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void getTemperatureWithCorrectLocationAndNullDateShouldBeCorrectResult() {
        String location = "location";
        TemperatureEntity entity = new TemperatureEntity();
        entity.setId(1L);
        entity.setCity("location");
        entity.setTemperature(36.6);
        entity.setTimeCreate(LocalDateTime.of(2022, 9, 12, 2, 26));

        when(temperatureRepository
                .findDistinctFirstByCityOrCountryOrderByTimeCreateDesc(location, location))
                .thenReturn(java.util.Optional.of(entity));
        String actualResult = temperatureService.getTemperature(location, null);

        String expectedResult = "36.6 | " + entity.getTimeCreate();
        Assertions.assertEquals(expectedResult, actualResult);
    }
}