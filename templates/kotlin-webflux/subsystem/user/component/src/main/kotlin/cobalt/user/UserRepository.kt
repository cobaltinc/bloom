package cobalt.user

import cobalt.user.model.User
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
internal class UserRepository {

  companion object {
    private val users = listOf(
      User(id = 1, familyName = "Talia", givenName = "Fritsch"),
      User(id = 2, familyName = "Daisy", givenName = "Lubowitz"),
      User(id = 3, familyName = "Chaz", givenName = "Rolfson"),
      User(id = 4, familyName = "Nelda", givenName = "Wolff"),
      User(id = 5, familyName = "Giovanna", givenName = "Nader"),
      User(id = 6, familyName = "Jerald", givenName = "Romaguera"),
      User(id = 7, familyName = "Faustino", givenName = "Nienow")
    )
  }

  fun findAll(): Flux<User> =
    Flux.fromIterable(users)

  fun findById(id: Int): Mono<User> {
    val user = users.find { it.id == id }
    return if (user != null) Mono.just(user) else Mono.empty()
  }

}
