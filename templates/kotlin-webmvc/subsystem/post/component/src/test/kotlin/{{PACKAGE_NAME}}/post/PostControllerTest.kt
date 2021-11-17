package {{PACKAGE_NAME}}.post

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest(PostController::class)
@Import(PostService::class)
internal class PostControllerTest {

  @MockBean
  lateinit var postRepository: PostRepository

  @Autowired
  lateinit var mockMvc: MockMvc

  @Autowired
  lateinit var mapper: ObjectMapper

  @Test
  fun findAllPost() {
    val posts = MockData.posts.toList()

    given(this.postRepository.findAll())
      .willReturn(posts)

    this.mockMvc
      .perform(get("/posts"))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(mapper.writeValueAsString(posts)))
  }

  @Test
  fun findPostById() {
    val post = MockData.posts[1]

    given(this.postRepository.findById(post.id))
      .willReturn(post)

    this.mockMvc
      .perform(get("/posts/${post.id}"))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(mapper.writeValueAsString(post)))
  }

  @Test
  fun notFound() {
    given(this.postRepository.findById(5))
      .willReturn(null)

    this.mockMvc
      .perform(get("/posts/5"))
      .andExpect(status().isNotFound)
  }

}
