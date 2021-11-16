package cobalt.post;

import cobalt.post.model.Post;

import java.util.List;

public interface PostProvided {
  List<Post> findByUserId(Integer userId);
}
