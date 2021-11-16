package cobalt.user;

import cobalt.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

  List<User> findAll() {
    return users;
  }

  Optional<User> findById(Integer id) {
    return users
      .stream()
      .filter(it -> it.id.equals(id))
      .findFirst();
  }

}
