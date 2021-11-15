package cobalt.user

import cobalt.post.PostProvided
import cobalt.post.model.Post
import cobalt.user.contract.UserWithPosts
import cobalt.user.model.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@ExtendWith(SpringExtension::class)
@WebFluxTest(UserController::class)
@Import(UserService::class)
internal class UserControllerTest {

  @MockBean
  lateinit var userRepository: UserRepository

  @MockBean
  lateinit var postProvided: PostProvided

  @Autowired
  lateinit var webTestClient: WebTestClient

  @Test
  fun findAllUser() {
    val users = MockData.users.toList()

    given(this.userRepository.findAll())
      .willReturn(Flux.fromIterable(users))

    this.webTestClient
      .get()
      .uri("/users")
      .exchange()
      .expectStatus().isOk
      .expectBodyList(User::class.java)
      .isEqualTo<Nothing>(users)
  }

  @Test
  fun findUserById() {
    val user = MockData.users[0]
    val posts = MockData.posts.filter { it.userId == user.id }

    given(this.userRepository.findById(user.id))
      .willReturn(Mono.just(user))

    given(this.postProvided.findByUserId(user.id))
      .willReturn(Flux.fromIterable(posts))

    this.webTestClient
      .get()
      .uri("/users/${user.id}")
      .exchange()
      .expectStatus().isOk
      .expectBody(UserWithPosts.Res::class.java)
      .isEqualTo<Nothing>(UserWithPosts.Res.from(user, posts))
  }

  @Test
  fun notFound() {
    given(this.userRepository.findById(5))
      .willReturn(Mono.empty())

    this.webTestClient
      .get()
      .uri("/users/5")
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun findAllPostByUser() {
    val userId = 1
    val userPosts = MockData.posts.filter { it.userId == userId }

    given(this.postProvided.findByUserId(userId))
      .willReturn(Flux.fromIterable(userPosts))

    this.webTestClient
      .get()
      .uri("/users/$userId/posts")
      .exchange()
      .expectStatus().isOk
      .expectBodyList(Post::class.java)
      .isEqualTo<Nothing>(userPosts)
  }

}