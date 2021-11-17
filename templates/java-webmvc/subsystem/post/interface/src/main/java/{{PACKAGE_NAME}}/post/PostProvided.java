package {{PACKAGE_NAME}}.post;

import {{PACKAGE_NAME}}.post.model.Post;

import java.util.List;

public interface PostProvided {
  List<Post> findByUserId(Integer userId);
}
