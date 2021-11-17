package {{PACKAGE_NAME}}.user;

import {{PACKAGE_NAME}}.post.model.Post;
import {{PACKAGE_NAME}}.user.model.User;

import java.util.List;

public class MockData {

  static final List<User> users = List.of(
    new User(1, "Talia", "Fritsch"),
    new User(2, "Daisy", "Lubowitz"),
    new User(3, "Chaz", "Rolfson")
  );

  static final List<Post> posts = List.of(
    new Post(1, 1, "Lorem ipsum dolor sit amet.", "Curabitur ultrices lacus lorem, vitae tincidunt lorem convallis."),
    new Post(2, 1, "Maecenas maximus sodales nibh vel.", "Pellentesque scelerisque tortor nec lorem volutpat, sed semper."),
    new Post(3, 3, "Sed tempus, justo at viverra.", "Cras efficitur, nunc eu maximus congue, magna quam.")
  );

}
