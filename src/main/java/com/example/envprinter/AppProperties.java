package com.example.envprinter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(String runtimeEnvName, String profileName) {
}
