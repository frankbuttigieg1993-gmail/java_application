# Env Printer Spring Boot Project

This app:
- starts a Spring Boot web server
- exposes `/health`
- logs `APP_RUNTIME_VALUE` every 5 seconds
- includes Dev / Staging / Prod profile files
- includes unit tests
- generates JaCoCo coverage reports
- includes a Dockerfile

## Run locally

```bash
APP_RUNTIME_VALUE=hello ./gradlew bootRun
```

## Build and test

```bash
./gradlew clean test jacocoTestReport jacocoTestCoverageVerification
```

## Docker

```bash
docker build -t env-printer .
docker run -e APP_RUNTIME_VALUE=hello -p 8080:8080 env-printer
```
