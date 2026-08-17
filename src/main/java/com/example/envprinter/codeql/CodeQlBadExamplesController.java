package com.example.envprinter.codeql;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.InitialContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

/**
 * INTENTIONALLY VULNERABLE CODE.
 *
 * This controller contains deliberately insecure examples for demonstrating
 * GitHub CodeQL security scanning.
 *
 * DO NOT use these patterns in production code.
 */
@RestController
public class CodeQlBadExamplesController {

    private static final Logger log =
            LoggerFactory.getLogger(CodeQlBadExamplesController.class);

    private final HttpClient httpClient = HttpClient.newHttpClient();

    /*
     * BAD EXAMPLE 1
     * CodeQL: java/command-line-injection
     *
     * User input flows directly into Runtime.exec().
     */
    @GetMapping("/codeql/command")
    public String commandInjection(HttpServletRequest request) throws Exception {

        String command = request.getParameter("command");

        // BAD: attacker-controlled input is executed as an OS command.
        Process process = Runtime.getRuntime().exec(command);

        return "Started process: " + process.pid();
    }

    /*
     * BAD EXAMPLE 2
     * CodeQL: java/path-injection
     *
     * User input becomes a filesystem path without validation.
     */
    @GetMapping("/codeql/file")
    public String pathInjection(HttpServletRequest request) throws IOException {

        String filename = request.getParameter("filename");

        // BAD: user-controlled path can escape the intended directory.
        return Files.readString(Path.of(filename));
    }

    /*
     * BAD EXAMPLE 3
     * CodeQL: java/ssrf
     *
     * User input controls the destination of an outbound HTTP request.
     */
    @GetMapping("/codeql/fetch")
    public String ssrf(HttpServletRequest request) throws Exception {

        String url = request.getParameter("url");

        // BAD: arbitrary user-controlled URL.
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return httpClient.send(
                httpRequest,
                HttpResponse.BodyHandlers.ofString()
        ).body();
    }

    /*
     * BAD EXAMPLE 4
     * CodeQL: java/xss
     *
     * User input is written directly into an HTML response.
     */
    @GetMapping("/codeql/xss")
    public void xss(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String name = request.getParameter("name");

        response.setContentType("text/html");

        // BAD: untrusted input is written directly to the page.
        response.getWriter().print(
                "<html><body>Hello " + name + "</body></html>"
        );
    }

    /*
     * BAD EXAMPLE 5
     * CodeQL: java/log-injection
     *
     * User-controlled data is written directly into application logs.
     */
    @GetMapping("/codeql/log")
    public String logInjection(HttpServletRequest request) {

        String message = request.getParameter("message");

        // BAD: user-controlled input is logged without sanitization.
        log.info("User message: {}", message);

        return "logged";
    }

    /*
     * BAD EXAMPLE 6
     * CodeQL: java/jndi-injection
     *
     * User input controls a JNDI lookup.
     */
    @GetMapping("/codeql/jndi")
    public String jndiInjection(HttpServletRequest request) throws Exception {

        String name = request.getParameter("name");

        InitialContext context = new InitialContext();

        // BAD: attacker-controlled JNDI lookup.
        Object result = context.lookup(name);

        return String.valueOf(result);
    }

    /*
     * BAD EXAMPLE 7
     * CodeQL: java/unsafe-deserialization
     *
     * Serialized data supplied by the HTTP request is passed directly
     * into ObjectInputStream.readObject().
     *
     * This is intentionally vulnerable for CodeQL demonstration purposes.
     */
    @GetMapping("/codeql/deserialize")
    public String unsafeDeserialization(HttpServletRequest request)
            throws Exception {

        // SOURCE: attacker-controlled HTTP parameter.
        String data = request.getParameter("data");

        // Convert the supplied Base64 string back into arbitrary bytes.
        byte[] bytes = Base64.getDecoder().decode(data);

        try (ByteArrayInputStream byteStream =
                     new ByteArrayInputStream(bytes);
             ObjectInputStream objectStream =
                     new ObjectInputStream(byteStream)) {

            // BAD:
            // Deserializing untrusted Java serialized data can result in
            // arbitrary object construction and potentially code execution.
            //
            // CodeQL should identify this as unsafe deserialization.
            Object object = objectStream.readObject();

            return String.valueOf(object);
        }
    }
}