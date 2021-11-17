package {{PACKAGE_NAME}}

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class {{APPLICATION_NAME}}Application

fun main(args: Array<String>) {
  runApplication<{{APPLICATION_NAME}}Application>(*args)
}
