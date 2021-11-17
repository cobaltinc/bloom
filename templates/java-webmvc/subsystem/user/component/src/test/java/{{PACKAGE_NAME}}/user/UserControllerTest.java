package {{PACKAGE_NAME}}.user;

import {{PACKAGE_NAME}}.post.PostProvided;
import {{PACKAGE_NAME}}.post.model.Post;
import {{PACKAGE_NAME}}.user.contract.UserWithPosts;
import {{PACKAGE_NAME}}.user.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@Import(UserService.class)
public class UserControllerTest {

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private PostProvided postProvided;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Test
  void findAllUser() throws Exception {
    final List<User> users = MockData.users;

    given(this.userRepository.findAll())
      .willReturn(users);

    this.mockMvc
      .perform(get("/users"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(mapper.writeValueAsString(users)));
  }

  @Test
  void findUserById() throws Exception {
    final User user = MockData.users.get(0);
    final List<Post> posts = MockData.posts
      .stream()
      .filter(post -> post.userId.equals(user.id))
      .collect(Collectors.toList());
    final UserWithPosts.Res expect = UserWithPosts.Res.from(user, posts);

    given(this.userRepository.findById(user.id))
      .willReturn(Optional.of(user));

    given(this.postProvided.findByUserId(user.id))
      .willReturn(posts);

    this.mockMvc
      .perform(get("/users/{id}", user.id))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(mapper.writeValueAsString(expect)));
  }

  @Test
  void notFound() throws Exception {
    given(this.userRepository.findById(5))
      .willReturn(Optional.empty());

    this.mockMvc
      .perform(get("/users/5"))
      .andExpect(status().isNotFound());
  }

  @Test
  void findAllPostByUser() throws Exception {
    final Integer userId = 1;
    final List<Post> posts = MockData.posts
      .stream()
      .filter(post -> post.userId.equals(userId))
      .collect(Collectors.toList());

    given(this.postProvided.findByUserId(userId))
      .willReturn(posts);

    this.mockMvc
      .perform(get("/users/{id}/posts", userId))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(mapper.writeValueAsString(posts)));
  }

}
