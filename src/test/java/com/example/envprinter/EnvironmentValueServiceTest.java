package com.example.envprinter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EnvironmentValueServiceTest {
    private final EnvironmentValueService service = new EnvironmentValueService();

    @Test
    void returnsNotSetForMissingVariable() {
        assertEquals("<not set>", service.resolveValue("THIS_VARIABLE_SHOULD_NOT_EXIST_12345"));
    }
}
