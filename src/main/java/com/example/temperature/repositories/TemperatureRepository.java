package com.example.temperature.repositories;

import com.example.temperature.entities.TemperatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface TemperatureRepository extends JpaRepository<TemperatureEntity, Long> {
    Collection<TemperatureEntity> findAllByTimeCreateBetweenAndCityOrCountry
            (LocalDateTime start, LocalDateTime end, String city, String country);

    Optional<TemperatureEntity> findDistinctFirstByCityOrCountryOrderByTimeCreateDesc(String city, String country);
}
