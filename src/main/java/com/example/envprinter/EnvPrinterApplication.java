package com.example.envprinter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EnvPrinterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnvPrinterApplication.class, args);
    }
}
