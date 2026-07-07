package com.example.envprinter.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RuntimeEnvScheduler {

    private final RuntimeEnvLogger runtimeEnvLogger;

    public RuntimeEnvScheduler(RuntimeEnvLogger runtimeEnvLogger) {
        this.runtimeEnvLogger = runtimeEnvLogger;
    }

    @Scheduled(fixedRate = 5000, initialDelay = 5000)
    public void logEveryFiveSeconds() {
        runtimeEnvLogger.logConfiguredEnvironmentVariable();
    }
}
