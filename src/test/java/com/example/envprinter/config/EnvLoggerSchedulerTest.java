package com.example.envprinter.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EnvLoggerSchedulerTest {

    @Test
    void schedulerClassExists() {
        assertThat(new EnvLoggerScheduler()).isNotNull();
    }
}
