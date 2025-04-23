import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"

    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.jetbrains.kotlin:kotlin-bom:1.9.25")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.1")
        mavenBom(SpringBootPlugin.BOM_COORDINATES)
    }
    dependencies {
        dependency("com.ninja-squad:springmockk:4.0.2")
        dependency("io.mockk:mockk-jvm:1.14.0")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    testImplementation("com.ninja-squad:springmockk")
    testImplementation("io.mockk:mockk-jvm")
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "21"
            freeCompilerArgs += "-Xjsr305=strict"
            javaParameters = true
        }
    }
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            events(SKIPPED, FAILED)
            showExceptions = true
            showStackTraces = true
            exceptionFormat = FULL
        }
    }
}
