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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
      .willReturn(users);

    assertEquals(this.userService.findAll(), users);
  }

  @Test
  void findById() {
    final User user = MockData.users.get(0);
    final List<Post> posts = MockData.posts
      .stream()
      .filter(post -> post.userId.equals(user.id))
      .collect(Collectors.toList());

    given(this.userRepository.findById(user.id))
      .willReturn(Optional.of(user));

    given(this.postProvided.findByUserId(user.id))
      .willReturn(posts);

    assertEquals(this.userService.findById(user.id), UserWithPosts.Res.from(user, posts));
  }

  @Test
  void notFound() {
    given(this.userRepository.findById(5))
      .willReturn(Optional.empty());

    assertThrows(ResponseStatusException.class, () -> this.userService.findById(5));
  }

  @Test
  void findAllPostByUser() {
    final Integer userId = 1;
    final List<Post> posts = MockData.posts
      .stream()
      .filter(post -> post.userId.equals(userId))
      .collect(Collectors.toList());

    given(this.postProvided.findByUserId(userId))
      .willReturn(posts);

    assertEquals(this.userService.findAllPostByUser(userId), posts);
  }

}
