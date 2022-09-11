package com.example.temperature.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/temperature")
@RequiredArgsConstructor
public class TemperatureController {

    @GetMapping("/update/all")
    public ResponseEntity<String> updateTemperature() {
        return ResponseEntity.ok("leo test ok");
    }
}
