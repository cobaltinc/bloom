package cobalt.user

import cobalt.post.PostProvided
import cobalt.post.model.Post
import cobalt.user.contract.UserWithPosts
import cobalt.user.model.User
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
internal class UserService(
  private val userRepository: UserRepository,
  private val postProvided: PostProvided
) {

  fun findAll(): Flux<User> =
    this.userRepository.findAll()

  fun findById(id: Int): Mono<UserWithPosts.Res> =
    this.userRepository
      .findById(id)
      .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND)))
      .flatMap { user ->
        this.postProvided
          .findByUserId(user.id)
          .collectList()
          .map { posts ->
            UserWithPosts.Res.from(user, posts)
          }
      }

  fun findAllPostByUser(userId: Int): Flux<Post> =
    this.postProvided.findByUserId(userId)

}
