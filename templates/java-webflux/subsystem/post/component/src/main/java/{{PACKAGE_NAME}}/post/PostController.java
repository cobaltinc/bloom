package {{PACKAGE_NAME}}.post;

import {{PACKAGE_NAME}}.post.model.Post;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/posts")
class PostController {

  private PostService postService;

  PostController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping
  Flux<Post> findAllPost() {
    return this.postService.findAll();
  }

  @GetMapping("/{id}")
  Mono<Post> findPostById(@PathVariable Integer id) {
    return this.postService.findById(id);
  }

}
