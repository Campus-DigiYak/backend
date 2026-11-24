package com.digiyak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulithic;

/**
 * Main entry point for the Digiyak Spring Boot application.
 *
 * <p><b>Why {@code @Modulithic}?</b>
 * Instead of deploying 5 separate microservices (which adds network latency and operational overhead),
 * Digiyak uses a <b>Modular Monolith</b> powered by Spring Modulith. Each business domain
 * ({@code identity}, {@code campus}, {@code delivery}, {@code dispatch}, {@code finance}) lives in its own
 * isolated package and PostgreSQL schema, while {@code shared} is declared as a shared module
 * accessible to all domains.
 */
@Modulithic(
        systemName = "Digiyak Campus Walking Delivery Network",
        sharedModules = "shared"
)
@SpringBootApplication
public class DigiyakApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigiyakApplication.class, args);
    }
}
