package cobalt.user;

import cobalt.post.PostProvided;
import cobalt.post.model.Post;
import cobalt.user.contract.UserWithPosts;
import cobalt.user.model.User;
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
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PostProvided postProvided;

  @InjectMocks
  private UserService userService;

  @Test
  void findAll() {
    final List<User> users = MockData.users;

    given(this.userRepository.findAll())
      .willReturn(Flux.fromIterable(users));

    StepVerifier.create(this.userService.findAll())
      .expectNext(users.toArray(new User[0]))
      .verifyComplete();
  }

  @Test
  void findById() {
    final User user = MockData.users.get(0);
    final List<Post> posts = MockData.posts
      .stream()
      .filter(post -> post.userId.equals(user.id))
      .collect(Collectors.toList());

    given(this.userRepository.findById(user.id))
      .willReturn(Mono.just(user));

    given(this.postProvided.findByUserId(user.id))
      .willReturn(Flux.fromIterable(posts));

    StepVerifier.create(this.userService.findById(user.id))
      .expectNext(UserWithPosts.Res.from(user, posts))
      .verifyComplete();
  }

  @Test
  void notFound() {
    given(this.userRepository.findById(5))
      .willReturn(Mono.empty());

    StepVerifier.create(this.userService.findById(5))
      .expectError(ResponseStatusException.class)
      .verify();
  }

  @Test
  void findAllPostByUser() {
    final Integer userId = 1;
    final List<Post> posts = MockData.posts
      .stream()
      .filter(post -> post.userId.equals(userId))
      .collect(Collectors.toList());

    given(this.postProvided.findByUserId(userId))
      .willReturn(Flux.fromIterable(posts));

    StepVerifier.create(this.userService.findAllPostByUser(userId))
      .expectNext(posts.toArray(new Post[0]))
      .verifyComplete();
  }

}
