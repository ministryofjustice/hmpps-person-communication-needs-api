import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "10.5.0"
  kotlin("jvm") version "2.4.0"
  kotlin("plugin.spring") version "2.4.0"
  jacoco
  idea
}

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencyCheck {
  suppressionFiles.add("azure-dependency-check-suppress.xml")
}

dependencies {
  implementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter:2.5.0")
  implementation("org.springframework.boot:spring-boot-starter-jackson")
  implementation("org.springframework.boot:spring-boot-starter-webclient")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("io.sentry:sentry-spring-boot-4:8.43.2")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.3")
  constraints {
    implementation("org.webjars:swagger-ui:5.32.2")
  }

  testImplementation("uk.gov.justice.service.hmpps:hmpps-kotlin-spring-boot-starter-test:2.5.0")
  testImplementation("org.springframework.boot:spring-boot-starter-jackson-test")
  testImplementation("org.springframework.boot:spring-boot-starter-webclient-test")
  testImplementation("org.springframework.boot:spring-boot-webtestclient")
  testImplementation("org.wiremock:wiremock-standalone:3.13.2")
  testImplementation("io.swagger.parser.v3:swagger-parser:2.1.43") {
    exclude(group = "io.swagger.core.v3")
  }
}

kotlin {
  jvmToolchain(25)
  compilerOptions {
    freeCompilerArgs.addAll("-Xannotation-default-target=param-property")
  }
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
      jvmTarget = JvmTarget.JVM_25
      freeCompilerArgs.add("-Xannotation-default-target=param-property")
    }
  }
}
