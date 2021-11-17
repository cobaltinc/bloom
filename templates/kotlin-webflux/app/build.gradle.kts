import java.io.File

val projectPath: String = File(System.getProperty("user.dir")).absolutePath
val subsystemPath = "$projectPath/subsystem"
val buildDirectory = listOf("build", "out", "bin")

val subsystems = File(subsystemPath).listFiles()
  ?.filter { it.isDirectory && !buildDirectory.contains(it.name) && !it.name.startsWith(".") }
  ?.map { it.name }

dependencies {
  subsystems?.forEach { subsystem ->
    this.implementation(project(":subsystem:$subsystem:$subsystem-component"))
  }
}
