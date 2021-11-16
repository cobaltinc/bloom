package cobalt.user;

import cobalt.user.model.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
class UserRepository {

  static List<User> users = List.of(
    new User(1, "Talia", "Fritsch"),
    new User(2, "Daisy", "Lubowitz"),
    new User(3, "Chaz", "Rolfson"),
    new User(4, "Nelda", "Wolff"),
    new User(5, "Giovanna", "Nader"),
    new User(6, "Jerald", "Romaguera"),
    new User(7, "Faustino", "Nienow")
  );

  Flux<User> findAll() {
    return Flux.fromIterable(users);
  }

  Mono<User> findById(Integer id) {
    final User user = users
      .stream()
      .filter(it -> it.id.equals(id))
      .findFirst()
      .orElse(null);

    if (user != null) {
      return Mono.just(user);
    } else {
      return Mono.empty();
    }
  }

}
