package cobalt.post

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@ExtendWith(SpringExtension::class)
internal class PostServiceTest {

  @Mock
  lateinit var postRepository: PostRepository

  @InjectMocks
  lateinit var postService: PostService

  @Test
  fun findAll() {
    val posts = MockData.posts

    given(this.postRepository.findAll())
      .willReturn(Flux.fromIterable(posts))

    StepVerifier.create(this.postService.findAll())
      .expectNext(*posts.toTypedArray())
      .verifyComplete()
  }

  @Test
  fun findByUserId() {
    val userId = 1
    val posts = MockData.posts.filter { it.userId == userId }

    given(this.postRepository.findByUserId(userId))
      .willReturn(Flux.fromIterable(posts))

    StepVerifier.create(this.postService.findByUserId(1))
      .expectNext(*posts.toTypedArray())
      .verifyComplete()
  }

  @Test
  fun findById() {
    val post = MockData.posts[2]

    given(this.postRepository.findById(post.id))
      .willReturn(Mono.just(post))

    StepVerifier.create(this.postService.findById(post.id))
      .expectNext(post)
      .verifyComplete()
  }

  @Test
  fun notFound() {
    given(this.postRepository.findById(5))
      .willReturn(Mono.empty())

    StepVerifier.create(this.postService.findById(5))
      .expectError(ResponseStatusException::class.java)
      .verify()
  }

}