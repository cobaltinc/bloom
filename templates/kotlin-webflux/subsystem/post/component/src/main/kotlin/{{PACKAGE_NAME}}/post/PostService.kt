package {{PACKAGE_NAME}}.post

import {{PACKAGE_NAME}}.post.model.Post
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
internal class PostService(
  private val postRepository: PostRepository
): PostProvided {

  fun findAll(): Flux<Post> =
    this.postRepository.findAll()

  override fun findByUserId(userId: Int): Flux<Post> =
    this.postRepository.findByUserId(userId)

  fun findById(id: Int): Mono<Post> =
    this.postRepository
      .findById(id)
      .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND)))

}
