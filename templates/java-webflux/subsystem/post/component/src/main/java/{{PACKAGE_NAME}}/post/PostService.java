package {{PACKAGE_NAME}}.post;

import {{PACKAGE_NAME}}.post.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
class PostService implements PostProvided {

  private PostRepository postRepository;

  PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  Flux<Post> findAll() {
    return this.postRepository.findAll();
  }

  public Flux<Post> findByUserId(Integer userId) {
    return this.postRepository.findByUserId(userId);
  }

  Mono<Post> findById(Integer id) {
    return this.postRepository
      .findById(id)
      .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }
}
