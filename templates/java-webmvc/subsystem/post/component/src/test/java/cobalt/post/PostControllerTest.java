package cobalt.post;

import cobalt.post.model.Post;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PostController.class)
@Import(PostService.class)
class PostControllerTest {

  @MockBean
  private PostRepository postRepository;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @Test
  void findAllPost() throws Exception {
    final List<Post> posts = MockData.posts;

    given(this.postRepository.findAll())
      .willReturn(posts);

    this.mockMvc
      .perform(get("/posts"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(mapper.writeValueAsString(posts)));
  }

  @Test
  void findPostById() throws Exception {
    final Post post = MockData.posts.get(1);

    given(this.postRepository.findById(post.id))
      .willReturn(Optional.of(post));

    this.mockMvc
      .perform(get("/posts/" + post.id))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(mapper.writeValueAsString(post)));
  }

  @Test
  void notFound() throws Exception {
    given(this.postRepository.findById(5))
      .willReturn(Optional.empty());

    this.mockMvc
      .perform(get("/posts/5"))
      .andExpect(status().isNotFound());
  }

}
