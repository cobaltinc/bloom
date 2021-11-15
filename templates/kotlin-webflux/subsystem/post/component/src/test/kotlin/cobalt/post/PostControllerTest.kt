package cobalt.post

import cobalt.post.model.Post
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
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
@WebFluxTest(PostController::class)
@Import(PostService::class)
internal class PostControllerTest {

  @MockBean
  lateinit var postRepository: PostRepository

  @Autowired
  lateinit var webTestClient: WebTestClient

  @Test
  fun findAllPost() {
    val posts = MockData.posts.toList()

    given(this.postRepository.findAll())
      .willReturn(Flux.fromIterable(posts))

    this.webTestClient
      .get()
      .uri("/posts")
      .exchange()
      .expectStatus().isOk
      .expectBodyList(Post::class.java)
      .isEqualTo<Nothing>(posts)
  }

  @Test
  fun findPostById() {
    val post = MockData.posts[1]

    given(this.postRepository.findById(post.id))
      .willReturn(Mono.just(post))

    this.webTestClient
      .get()
      .uri("/posts/${post.id}")
      .exchange()
      .expectStatus().isOk
      .expectBody(Post::class.java)
      .isEqualTo<Nothing>(post)
  }

  @Test
  fun notFound() {
    given(this.postRepository.findById(5))
      .willReturn(Mono.empty())

    this.webTestClient
      .get()
      .uri("/posts/5")
      .exchange()
      .expectStatus().isNotFound
  }

}
