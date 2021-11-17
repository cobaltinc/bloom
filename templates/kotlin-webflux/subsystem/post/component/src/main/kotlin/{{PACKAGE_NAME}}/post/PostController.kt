package {{PACKAGE_NAME}}.post

import {{PACKAGE_NAME}}.post.model.Post
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/posts")
internal class PostController(
  private val postService: PostService
) {

  @GetMapping
  fun findAllPost(): Flux<Post> =
    this.postService.findAll()

  @GetMapping("/{id}")
  fun findPostById(@PathVariable id: Int): Mono<Post> =
    this.postService.findById(id)

}
