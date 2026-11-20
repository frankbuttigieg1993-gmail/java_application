package com.example.envprinter;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class ApplicationContextTest {
    @MockitoBean
    private DataSource dataSource;

    @Test
    void contextLoads() {
    }
}
