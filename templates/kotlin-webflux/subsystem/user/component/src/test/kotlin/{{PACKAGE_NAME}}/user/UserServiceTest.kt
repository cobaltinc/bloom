package {{PACKAGE_NAME}}.user

import {{PACKAGE_NAME}}.post.PostProvided
import {{PACKAGE_NAME}}.user.contract.UserWithPosts
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(SpringExtension::class)
internal class UserServiceTest {

  @Mock
  lateinit var userRepository: UserRepository

  @Mock
  lateinit var postProvided: PostProvided

  @InjectMocks
  lateinit var userService: UserService

  @Test
  fun findAll() {
    val users = MockData.users

    given(this.userRepository.findAll())
      .willReturn(Flux.fromIterable(users))

    StepVerifier.create(this.userService.findAll())
      .expectNext(*users.toTypedArray())
      .verifyComplete()
  }

  @Test
  fun findById() {
    val user = MockData.users[0]
    val posts = MockData.posts.filter { it.userId == user.id }

    given(this.userRepository.findById(user.id))
      .willReturn(Mono.just(user))

    given(this.postProvided.findByUserId(user.id))
      .willReturn(Flux.fromIterable(posts))

    StepVerifier.create(this.userService.findById(user.id))
      .expectNext(UserWithPosts.Res.from(user, posts))
      .verifyComplete()
  }

  @Test
  fun notFound() {
    given(this.userRepository.findById(5))
      .willReturn(Mono.empty())

    StepVerifier.create(this.userService.findById(5))
      .expectError(ResponseStatusException::class.java)
      .verify()
  }

  @Test
  fun findAllPostByUser() {
    val posts = MockData.posts.filter { it.userId == 1 }

    given(this.postProvided.findByUserId(1))
      .willReturn(Flux.fromIterable(posts))

    StepVerifier.create(this.userService.findAllPostByUser(1))
      .expectNext(*posts.toTypedArray())
      .verifyComplete()
  }
}