import java.io.File

rootProject.name = "{{PROJECT_NAME}}"

pluginManagement {
  repositories {
    maven { url = uri("https://repo.spring.io/milestone") }
    gradlePluginPortal()
  }
  resolutionStrategy {
    eachPlugin {
      if (requested.id.id == "org.springframework.boot") {
        useModule("org.springframework.boot:spring-boot-gradle-plugin:${requested.version}")
      }
    }
  }
}

include(":app")

val projectPath: String = File(System.getProperty("user.dir")).absolutePath
val subsystemPath = "$projectPath/subsystem"

val subsystems = File(subsystemPath).listFiles()
  ?.filter { it.isDirectory }
  ?.filter { it.name != "build" && it.name != "out" && it.name != "bin" }
  ?.map { it.name }

subsystems?.forEach { subsystem ->
  println("Loaded $subsystem subsystem.")

  include(":subsystem:$subsystem:interface")
  project(":subsystem:$subsystem:interface").name = "$subsystem-interface"
  include(":subsystem:$subsystem:component")
  project(":subsystem:$subsystem:component").name = "$subsystem-component"
}
