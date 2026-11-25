# Digiyak backend

Java 25 / Spring Boot API. For the complete multi-repository local setup, including PostgreSQL/PostGIS and Redis, see the [infra development guide](https://github.com/Campus-DigiYak/infra/blob/dev/README.md).

## Prerequisites

- JDK 25 or newer (JDK 26 works fine — the build uses `--release 25`)
- PostgreSQL with PostGIS and Redis running locally (the infra repository provides Docker Compose)

The project includes a Gradle wrapper pinned to Gradle 9.9.2; installing Gradle globally is unnecessary.

## Run

Start the database and Redis using the infra guide, then run from this repository:

Windows PowerShell:

```powershell
.\gradlew.bat bootRun
```

macOS/Linux:

```sh
./gradlew bootRun
```

> **Note:** Gradle will show `> :bootRun` stuck at roughly 80% — this is normal. The web server runs indefinitely inside that Gradle task, so the progress bar never reaches 100%. Once you see `Started DigiyakApplication in …` in the log output, the API is ready. Press `Ctrl+C` to stop it.

The API listens on port `8080`. Default local database settings use PostgreSQL at `localhost:5432` and Redis at `localhost:6379`. Spring Boot reads environment variables from the process; it does not automatically load a `.env` file. The defaults in `src/main/resources/application.yml` match the local Compose defaults.

## Test

Windows PowerShell:

```powershell
.\gradlew.bat test
```

macOS/Linux:

```sh
./gradlew test
```

## API endpoints

- Swagger UI: <http://localhost:8080/swagger-ui.html>
- OpenAPI JSON: <http://localhost:8080/v3/api-docs>
- Actuator health: <http://localhost:8080/actuator/health>
