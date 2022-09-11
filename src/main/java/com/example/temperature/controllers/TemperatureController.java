package com.example.temperature.controllers;

import com.example.temperature.services.TemperatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/temperature")
@RequiredArgsConstructor
public class TemperatureController {
    private final TemperatureService temperatureService;

    @GetMapping
    public ResponseEntity<String> getTemperature(
            @RequestParam String location,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        String result = temperatureService.getTemperature(location, date);

        return ResponseEntity.ok(result);
    }
}
