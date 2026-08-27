package com.example.envprinter.codeql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * INTENTIONALLY VULNERABLE CODEQL VALIDATION CASE.
 *
 * Add this file only to the PR/source branch so that the SQL injection is a
 * newly introduced issue relative to the PR target branch.
 *
 * DO NOT MERGE OR DEPLOY THIS FILE.
 * Delete it after validating the CodeQL workflow.
 */
@RestController
public class CodeQlNewSqlInjectionController {

    private final DataSource dataSource;

    public CodeQlNewSqlInjectionController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
     * Intentionally vulnerable to SQL injection.
     *
     * Data flow:
     * HTTP request parameter -> SQL string concatenation -> Statement.executeQuery()
     */
    @GetMapping("/codeql-test/sql-injection")
    public String sqlInjection(HttpServletRequest request) throws Exception {
        String username = request.getParameter("username");

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