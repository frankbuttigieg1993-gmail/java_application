package com.example.envprinter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EnvLoggerScheduler {

    private static final Logger log = LoggerFactory.getLogger(EnvLoggerScheduler.class);
    private static final String VARIABLE_NAME = "APP_RUNTIME_VALUE";

    @Scheduled(fixedDelay = 5000)
    public void logEnvironmentVariable() {
        String value = System.getenv(VARIABLE_NAME);
        log.info("{} = {}", VARIABLE_NAME, value != null ? value : "<not set>");
    }
}
