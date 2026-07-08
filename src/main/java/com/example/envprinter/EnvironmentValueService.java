package com.example.envprinter;

import org.springframework.stereotype.Service;

@Service
public class EnvironmentValueService {
    public String resolveValue(String variableName) {
        String value = System.getenv(variableName);
        return (value == null || value.isBlank()) ? "<not set>" : value;
    }
}
