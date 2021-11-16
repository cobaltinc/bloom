import java.io.File

val projectPath: String = File(System.getProperty("user.dir")).absolutePath
val subsystemPath = "$projectPath/subsystem"

val subsystems = File(subsystemPath).listFiles()
  ?.filter { it.isDirectory }
  ?.filter { it.name != "build" && it.name != "out" && it.name != "bin" }
  ?.map { it.name }

dependencies {
  subsystems?.forEach { subsystem ->
    this.implementation(project(":subsystem:$subsystem:$subsystem-component"))
  }
}
