package com.example.envprinter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class RuntimeEnvLogger {

    private static final Logger log = LoggerFactory.getLogger(RuntimeEnvLogger.class);

    private final Environment environment;
    private final String envVarName;

    public RuntimeEnvLogger(Environment environment,
                            @Value("${app.runtime-env-name:APP_RUNTIME_VALUE}") String envVarName) {
        this.environment = environment;
        this.envVarName = envVarName;
    }

    public String readRuntimeValue() {
        return environment.getProperty(envVarName);
    }

    public String describeRuntimeValue() {
        String value = readRuntimeValue();
        if (value == null || value.isBlank()) {
            return "Environment variable " + envVarName + " is not set";
        }
        return "Environment variable " + envVarName + "=" + value;
    }

    public void logConfiguredEnvironmentVariable() {
        log.info("{}", describeRuntimeValue());
    }

    public String getEnvVarName() {
        return envVarName;
    }
}
