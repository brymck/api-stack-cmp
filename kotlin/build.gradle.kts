import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Apply Spring plugins
    id("org.springframework.boot") version "2.1.8.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"

    // Apply the Kotlin JVM plugin to add support for Kotlin and Spring
    kotlin("jvm") version "1.3.50"
    kotlin("plugin.spring") version "1.3.50"

    // Lint code with ktlint
    // id("org.jlleitschuh.gradle.ktlint") version "9.1.1"
    // id("org.jlleitschuh.gradle.ktlint-idea") version "9.1.1"

    // Test reporting
    id("com.adarshr.test-logger") version "2.0.0"

    // Provide unit test coverage with JaCoCo
    jacoco
}

group = "com.github.brymck"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

repositories {
    flatDir {
        dirs("../openapi/build/greeting/build/libs")
    }
    jcenter()
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Gson
    implementation("com.google.code.gson:gson:2.8.6")

    // Kotlin
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Logging
    implementation("io.github.microutils:kotlin-logging:1.7.7")

    // Kotlin test integration with JUnit
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")

    // MockK
    testImplementation("io.mockk:mockk:1.9.3")

    // AssertJ for fluent assertions
    testImplementation("org.assertj:assertj-core:3.11.1")

    // Spring Boot testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks {
    compileKotlin {
        // dependsOn(ktlintFormat)
    }

    // Force a JVM target of 1.8
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    // Use JUnit for testing
    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }

    // Run coverage
    jacocoTestReport {
        reports {
            xml.isEnabled = true
            html.isEnabled = true
            csv.isEnabled = false
        }
    }
}
