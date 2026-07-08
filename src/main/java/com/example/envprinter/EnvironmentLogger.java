package com.example.envprinter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentLogger {
    private static final Logger log = LoggerFactory.getLogger(EnvironmentLogger.class);

    private final AppProperties properties;
    private final EnvironmentValueService valueService;

    public EnvironmentLogger(AppProperties properties, EnvironmentValueService valueService) {
        this.properties = properties;
        this.valueService = valueService;
    }

    @Scheduled(fixedRate = 5000)
    public void logRuntimeValue() {
        String variableName = properties.runtimeEnvName();
        String value = valueService.resolveValue(variableName);
        log.info("{}={}", variableName, value);
    }
}
