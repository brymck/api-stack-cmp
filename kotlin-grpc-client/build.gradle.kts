import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("application")
    id("idea")

    // Protobufs
    id("com.google.protobuf") version "0.8.8"

    // Apply the Kotlin JVM plugin to add support for Kotlin and Spring
    kotlin("jvm") version "1.3.50"

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
    jcenter()
    mavenCentral()
}

application {
    mainClassName = "com.github.brymck.greetings.client.App"
}

val grpcVersion = "1.25.0"
val protobufVersion = "3.11.0"
val protocVersion = protobufVersion

dependencies {
    // Protobufs
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")

    // Kotlin
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")

    // Logging
    implementation("io.github.microutils:kotlin-logging:1.7.7")
    implementation("org.slf4j:slf4j-simple:1.7.26")

    // REST API calls
    implementation("com.squareup.okhttp3:okhttp:4.2.0")
    implementation("com.google.code.gson:gson:2.8.6")

    // Shaded JARs
    runtimeOnly("io.grpc:grpc-netty-shaded:$grpcVersion")

    // Annotations
    compileOnly("javax.annotation:javax.annotation-api:1.2")

    // Kotlin test integration with JUnit
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    // JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")

    // MockK
    testImplementation("io.mockk:mockk:1.9.3")

    // AssertJ for fluent assertions
    testImplementation("org.assertj:assertj-core:3.11.1")

    // Protobuf testing
    testImplementation("io.grpc:grpc-testing:$grpcVersion")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protocVersion"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
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

    withType<Jar> {
        manifest {
            attributes["Main-Class"] = application.mainClassName
        }
        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
    }
}
