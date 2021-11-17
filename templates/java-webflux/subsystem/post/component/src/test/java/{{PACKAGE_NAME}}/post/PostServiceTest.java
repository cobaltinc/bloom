package {{PACKAGE_NAME}}.post;

import {{PACKAGE_NAME}}.post.model.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class PostServiceTest {

  @Mock
  private PostRepository postRepository;

  @InjectMocks
  private PostService postService;

  @Test
  void findAll() {
    final List<Post> posts = MockData.posts;

    given(this.postRepository.findAll())
      .willReturn(Flux.fromIterable(posts));

    StepVerifier.create(this.postService.findAll())
      .expectNext(posts.toArray(new Post[0]))
      .verifyComplete();
  }

  @Test
  void findByUserId() {
    final Integer userId = 1;
    final List<Post> posts = MockData.posts
      .stream()
      .filter(post -> post.id.equals(userId))
      .collect(Collectors.toList());

    given(this.postRepository.findByUserId(userId))
      .willReturn(Flux.fromIterable(posts));

    StepVerifier.create(this.postService.findByUserId(userId))
      .expectNext(posts.toArray(new Post[0]))
      .verifyComplete();
  }

  @Test
  void findById() {
    final Post post = MockData.posts.get(2);

    given(this.postRepository.findById(post.id))
      .willReturn(Mono.just(post));

    StepVerifier.create(this.postService.findById(post.id))
      .expectNext(post)
      .verifyComplete();
  }

  @Test
  void notFound() {
    given(this.postRepository.findById(5))
      .willReturn(Mono.empty());

    StepVerifier.create(this.postService.findById(5))
      .expectError(ResponseStatusException.class)
      .verify();
  }

}
