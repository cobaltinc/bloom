package cobalt.post;

import cobalt.post.model.Post;
import java.util.List;

public class MockData {
  static final List<Post> posts;

  static {
    posts = List.of(
      new Post(1, 1, "Lorem ipsum dolor sit amet.", "Curabitur ultrices lacus lorem, vitae tincidunt lorem convallis."),
      new Post(2, 1, "Maecenas maximus sodales nibh vel.", "Pellentesque scelerisque tortor nec lorem volutpat, sed semper."),
      new Post(3, 3, "Sed tempus, justo at viverra.", "Cras efficitur, nunc eu maximus congue, magna quam.")
    );
  }

}
