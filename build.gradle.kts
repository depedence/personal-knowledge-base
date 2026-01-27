plugins {
    java
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.depedence"
version = "0.0.1"
description = "pkb"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("com.h2database:h2")

    // Thymeleaf
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // Test
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    // selenium
    implementation("org.seleniumhq.selenium:selenium-java:4.40.0")
    implementation("io.github.bonigarcia:webdrivermanager:6.3.3")

    // rest assured
    testImplementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("io.rest-assured:json-schema-validator:5.4.0")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
