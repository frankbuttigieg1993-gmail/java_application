package com.example.envprinter.codeql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * INTENTIONALLY VULNERABLE CODEQL VALIDATION CASE.
 *
 * Keep this file only on the test/source branch.
 * DO NOT MERGE OR DEPLOY IT.
 */
@RestController
public class CodeQlNewSqlInjectionController {

    private final DataSource dataSource;

    public CodeQlNewSqlInjectionController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/codeql-test/sql-injection")
    public String sqlInjection(@RequestParam String username) throws Exception {
        String sql =
            "SELECT username FROM users WHERE username = '" +
            username +
            "'";

        try (
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)
        ) {
            return resultSet.next()
                ? resultSet.getString(1)
                : "not found";
        }
    }
}