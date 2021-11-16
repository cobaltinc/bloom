package cobalt.post;

import cobalt.post.model.Post;
import reactor.core.publisher.Flux;

public interface PostProvided {
  Flux<Post> findByUserId(Integer userId);
}
