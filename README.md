# Env Printer Spring Boot

A minimal Spring Boot project that:

- supports Dev, Staging, and Production environment files
- uses Gradle
- includes unit tests
- exposes a health endpoint
- logs the value of a runtime environment variable every 5 seconds
- includes a Dockerfile

## Runtime environment variable

By default the app looks for `APP_RUNTIME_VALUE`.

You can change the name in `src/main/resources/application.yml` under:

```yaml
app:
  runtime-env-name: APP_RUNTIME_VALUE
```

## Run locally

```bash
export APP_RUNTIME_VALUE=hello
gradle bootRun
```

To choose a profile:

```bash
gradle bootRun --args='--spring.profiles.active=dev'
gradle bootRun --args='--spring.profiles.active=staging'
gradle bootRun --args='--spring.profiles.active=prod'
```

The application runs continuously and prints the environment variable value every 5 seconds.

## Health check

- Custom health endpoint: `GET /health`
- Actuator health endpoint: `GET /actuator/health`

## Tests

```bash
gradle test
```

## Docker

Build and run:

```bash
docker build -t env-printer-springboot .
docker run --rm -e APP_RUNTIME_VALUE=hello env-printer-springboot
```
