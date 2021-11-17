package {{PACKAGE_NAME}}.user;

import {{PACKAGE_NAME}}.post.PostProvided;
import {{PACKAGE_NAME}}.post.model.Post;
import {{PACKAGE_NAME}}.user.contract.UserWithPosts;
import {{PACKAGE_NAME}}.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebFluxTest(UserController.class)
@Import(UserService.class)
public class UserControllerTest {

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private PostProvided postProvided;

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void findAllUser() {
    final List<User> users = MockData.users;

    given(this.userRepository.findAll())
      .willReturn(Flux.fromIterable(users));

    this.webTestClient
      .get()
      .uri("/users")
      .exchange()
      .expectStatus().isOk()
      .expectBodyList(User.class)
      .isEqualTo(users);
  }

  @Test
  void findUserById() {
    final User user = MockData.users.get(0);
    final List<Post> posts = MockData.posts
      .stream()
      .filter(post -> post.userId.equals(user.id))
      .collect(Collectors.toList());

    given(this.userRepository.findById(user.id))
      .willReturn(Mono.just(user));

    given(this.postProvided.findByUserId(user.id))
      .willReturn(Flux.fromIterable(posts));

    this.webTestClient
      .get()
      .uri("/users/" + user.id)
      .exchange()
      .expectStatus().isOk()
      .expectBody(UserWithPosts.Res.class)
      .isEqualTo(UserWithPosts.Res.from(user, posts));
  }

  @Test
  void notFound() {
    given(this.userRepository.findById(5))
      .willReturn(Mono.empty());

    this.webTestClient
      .get()
      .uri("/users/5")
      .exchange()
      .expectStatus().isNotFound();
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

    this.webTestClient
      .get()
      .uri("/users/" + userId + "/posts")
      .exchange()
      .expectStatus().isOk()
      .expectBodyList(Post.class)
      .isEqualTo(posts);
  }

}
