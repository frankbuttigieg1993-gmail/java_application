package com.example.envprinter.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RuntimeEnvStartupLogger implements ApplicationRunner {

    private final RuntimeEnvLogger runtimeEnvLogger;

    public RuntimeEnvStartupLogger(RuntimeEnvLogger runtimeEnvLogger) {
        this.runtimeEnvLogger = runtimeEnvLogger;
    }

    @Override
    public void run(ApplicationArguments args) {
        runtimeEnvLogger.logConfiguredEnvironmentVariable();
    }
}
