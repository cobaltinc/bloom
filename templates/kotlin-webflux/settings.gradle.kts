import java.io.File

rootProject.name = "{{PROJECT_NAME}}"

include(":app")

val projectPath: String = File(System.getProperty("user.dir")).absolutePath
val subsystemPath = "$projectPath/subsystem"
val buildDirectory = listOf("build", "out", "bin")

val subsystems = File(subsystemPath).listFiles()
  ?.filter { it.isDirectory && !buildDirectory.contains(it.name) && !it.name.startsWith(".") }
  ?.map { it.name }

subsystems?.forEach { subsystem ->
  println("Loaded $subsystem subsystem.")

  include(":subsystem:$subsystem:interface")
  project(":subsystem:$subsystem:interface").name = "$subsystem-interface"
  include(":subsystem:$subsystem:component")
  project(":subsystem:$subsystem:component").name = "$subsystem-component"
}
