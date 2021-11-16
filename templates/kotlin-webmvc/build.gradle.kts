plugins {
  kotlin("jvm") version "1.5.31"
  kotlin("plugin.spring") version "1.5.31"
  id("org.springframework.boot") version "2.5.6"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

allprojects {
  group = "{{PROJECT_NAME}}"
  version = "0.0.1"

  repositories {
    mavenCentral()
    gradlePluginPortal()
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
    dependsOn(tasks.processResources)
  }

  tasks.compileTestKotlin {
    kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=warn")
      jvmTarget = "15"
    }
    dependsOn(tasks.processResources)
  }

  tasks.jar {
    enabled = true
    archiveBaseName.set("${rootProject.name}-${project.name}")
  }

  tasks.test {
    useJUnitPlatform()
  }

  configurations {
    compileOnly {
      extendsFrom(configurations.annotationProcessor.get())
    }
  }

  dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
  }
}
