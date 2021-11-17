package {{PACKAGE_NAME}}.post;

import {{PACKAGE_NAME}}.post.model.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
      .willReturn(posts);

    assertEquals(this.postService.findAll(), posts);
  }

  @Test
  void findByUserId() {
    final Integer userId = 1;
    final List<Post> posts = MockData.posts
      .stream()
      .filter(post -> post.id.equals(userId))
      .collect(Collectors.toList());

    given(this.postRepository.findByUserId(userId))
      .willReturn(posts);

    assertEquals(this.postService.findByUserId(userId), posts);
  }

  @Test
  void findById() {
    final Post post = MockData.posts.get(2);

    given(this.postRepository.findById(post.id))
      .willReturn(Optional.of(post));

    assertEquals(this.postService.findById(post.id), post);
  }

  @Test
  void notFound() {
    given(this.postRepository.findById(5))
      .willReturn(Optional.empty());

    assertThrows(ResponseStatusException.class, () -> this.postService.findById(5));
  }

}
