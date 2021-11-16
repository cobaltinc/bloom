package cobalt.user

import cobalt.post.PostProvided
import cobalt.user.contract.UserWithPosts
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
@WebMvcTest(UserController::class)
@Import(UserService::class)
internal class UserControllerTest {

  @MockBean
  lateinit var userRepository: UserRepository

  @MockBean
  lateinit var postProvided: PostProvided

  @Autowired
  lateinit var mockMvc: MockMvc

  @Autowired
  lateinit var mapper: ObjectMapper

  @Test
  fun findAllUser() {
    val users = MockData.users.toList()

    given(this.userRepository.findAll())
      .willReturn(users)

    this.mockMvc
      .perform(get("/users"))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(mapper.writeValueAsString(users)))
  }

  @Test
  fun findUserById() {
    val user = MockData.users[0]
    val posts = MockData.posts.filter { it.userId == user.id }
    val expect = UserWithPosts.Res.from(user, posts)

    given(this.userRepository.findById(user.id))
      .willReturn(user)

    given(this.postProvided.findByUserId(user.id))
      .willReturn(posts)

    this.mockMvc
      .perform(get("/users/${user.id}"))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(mapper.writeValueAsString(expect)))
  }

  @Test
  fun notFound() {
    given(this.userRepository.findById(5))
      .willReturn(null)

    this.mockMvc
      .perform(get("/users/5"))
      .andExpect(status().isNotFound)
  }

  @Test
  fun findAllPostByUser() {
    val userId = 1
    val userPosts = MockData.posts.filter { it.userId == userId }

    given(this.postProvided.findByUserId(userId))
      .willReturn(userPosts)

    this.mockMvc
      .perform(get("/users/$userId/posts"))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(mapper.writeValueAsString(userPosts)))
  }

}