package {{PACKAGE_NAME}}.post;

import {{PACKAGE_NAME}}.post.model.Post;
import reactor.core.publisher.Flux;

public interface PostProvided {
  Flux<Post> findByUserId(Integer userId);
}
