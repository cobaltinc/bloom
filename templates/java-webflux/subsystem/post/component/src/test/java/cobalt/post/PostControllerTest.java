package cobalt.post;

import cobalt.post.model.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebFluxTest(PostController.class)
@Import(PostService.class)
class PostControllerTest {

  @MockBean
  private PostRepository postRepository;

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void findAllPost() {
    final List<Post> posts = MockData.posts;

    given(this.postRepository.findAll())
      .willReturn(Flux.fromIterable(posts));

    this.webTestClient
      .get()
      .uri("/posts")
      .exchange()
      .expectStatus().isOk()
      .expectBodyList(Post.class)
      .isEqualTo(posts);
  }

  @Test
  void findPostById() {
    final Post post = MockData.posts.get(1);

    given(this.postRepository.findById(post.id))
      .willReturn(Mono.just(post));

    this.webTestClient
      .get()
      .uri("/posts/" + post.id)
      .exchange()
      .expectStatus().isOk()
      .expectBody(Post.class)
      .isEqualTo(post);
  }

  @Test
  void notFound() {
    given(this.postRepository.findById(5))
      .willReturn(Mono.empty());

    this.webTestClient
      .get()
      .uri("/posts/5")
      .exchange()
      .expectStatus().isNotFound();
  }

}
