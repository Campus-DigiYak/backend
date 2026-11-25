plugins {
    java
    id("org.springframework.boot") version "4.2.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.digiyak"
version = "0.1.0-SNAPSHOT"

// Target Java 25 bytecode (uses --release 25 so JDK 25+ can compile — JDK 26 works fine)
java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

repositories {
    mavenCentral()
}

// Import Spring Modulith BOM so core, jpa, and test starters stay on matching 2.2.0 versions
dependencyManagement {
    imports {
        mavenBom("org.springframework.modulith:spring-modulith-bom:2.2.0")
    }
}

dependencies {
    // Spring Boot 4.2 Core Starters (Java 25 + Virtual Threads)
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-flyway")

    // Spring Modulith 2.2 (Strict Module Boundaries + Transactional Outbox)
    implementation("org.springframework.modulith:spring-modulith-starter-core")
    implementation("org.springframework.modulith:spring-modulith-starter-jpa")

    // Database: PostgreSQL + PostGIS (Hibernate Spatial) + Flyway
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.hibernate.orm:hibernate-spatial")
    runtimeOnly("org.postgresql:postgresql")

    // OpenAPI 3.1 / Swagger UI (Spring Boot 4.2 compatible)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.2.0")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.modulith:spring-modulith-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<JavaCompile> {
    options.release.set(25)
    // Treat all javac warnings (e.g. raw types, unchecked casts, deprecations) as build errors
    options.compilerArgs.addAll(listOf("-Xlint:all", "-Xlint:-processing", "-Werror"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
