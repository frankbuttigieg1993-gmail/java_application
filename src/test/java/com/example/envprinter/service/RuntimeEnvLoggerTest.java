package com.example.envprinter.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

class RuntimeEnvLoggerTest {

    @Test
    void readsConfiguredEnvironmentVariable() {
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty("MY_RUNTIME_VALUE", "hello-from-test");

        RuntimeEnvLogger logger = new RuntimeEnvLogger(environment, "MY_RUNTIME_VALUE");

        assertThat(logger.getEnvVarName()).isEqualTo("MY_RUNTIME_VALUE");
        assertThat(logger.readRuntimeValue()).isEqualTo("hello-from-test");
        assertThat(logger.describeRuntimeValue()).isEqualTo("Environment variable MY_RUNTIME_VALUE=hello-from-test");
    }

    @Test
    void returnsHelpfulMessageWhenEnvironmentVariableIsMissing() {
        MockEnvironment environment = new MockEnvironment();

        RuntimeEnvLogger logger = new RuntimeEnvLogger(environment, "MISSING_VALUE");

        assertThat(logger.readRuntimeValue()).isNull();
        assertThat(logger.describeRuntimeValue()).isEqualTo("Environment variable MISSING_VALUE is not set");
    }
}
