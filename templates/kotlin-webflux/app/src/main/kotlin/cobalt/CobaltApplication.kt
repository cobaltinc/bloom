package cobalt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CobaltApplication

fun main(args: Array<String>) {
  runApplication<CobaltApplication>(*args)
}
