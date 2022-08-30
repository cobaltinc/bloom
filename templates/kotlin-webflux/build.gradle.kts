plugins {
  kotlin("jvm") version "1.7.0"
  kotlin("plugin.spring") version "1.7.0"
  id("org.springframework.boot") version "2.5.6"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

allprojects {
  group = "{{PACKAGE_NAME}}"
  version = "0.0.1"

  repositories {
    mavenCentral()
  }
}

subprojects {
  apply(plugin = "kotlin")
  apply(plugin = "org.jetbrains.kotlin.plugin.spring")
  apply(plugin = "org.springframework.boot")
  apply(plugin = "io.spring.dependency-management")

  java.sourceCompatibility = JavaVersion.VERSION_15

  tasks.compileKotlin {
    kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=warn")
      jvmTarget = "15"
    }
  }

  tasks.compileTestKotlin {
    kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=warn")
      jvmTarget = "15"
    }
  }

  tasks.test {
    useJUnitPlatform()
  }

  dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
  }
}
