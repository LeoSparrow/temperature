package com.example.temperature.services.impl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = "fetch-rate=* * * * * *")
class EventServiceImplTest {
    @SpyBean
    private EventServiceImpl eventService;

    @Test
    void schedulerShouldBeCalledAfterOneSecond() {
        await().atMost(Duration.of(20000, ChronoUnit.MILLIS))
                .untilAsserted(() -> verify(eventService, atLeast(1)).updateTemperature());
    }
}