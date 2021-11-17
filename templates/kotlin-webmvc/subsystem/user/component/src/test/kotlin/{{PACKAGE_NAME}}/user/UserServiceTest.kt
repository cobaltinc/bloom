package {{PACKAGE_NAME}}.user

import {{PACKAGE_NAME}}.post.PostProvided
import {{PACKAGE_NAME}}.user.contract.UserWithPosts
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.server.ResponseStatusException

@ExtendWith(SpringExtension::class)
internal class UserServiceTest {

  @Mock
  lateinit var userRepository: UserRepository

  @Mock
  lateinit var postProvided: PostProvided

  @InjectMocks
  lateinit var userService: UserService

  @Test
  fun findAll() {
    val users = MockData.users

    given(this.userRepository.findAll())
      .willReturn(MockData.users)

    assertEquals(this.userService.findAll(), users)
  }

  @Test
  fun findById() {
    val user = MockData.users[0]
    val posts = MockData.posts.filter { it.userId == user.id }

    given(this.userRepository.findById(user.id))
      .willReturn(user)

    given(this.postProvided.findByUserId(user.id))
      .willReturn(posts)

    assertEquals(this.userService.findById(user.id), UserWithPosts.Res.from(user, posts))
  }

  @Test
  fun notFound() {
    given(this.userRepository.findById(5))
      .willReturn(null)

    assertThrows(ResponseStatusException(HttpStatus.NOT_FOUND)::class.java) { this.userService.findById(5) }
  }

  @Test
  fun findAllPostByUser() {
    val userId = 1
    val posts = MockData.posts.filter { it.userId == userId }

    given(this.postProvided.findByUserId(userId))
      .willReturn(posts)

    assertEquals(this.userService.findAllPostByUser(userId), posts)
  }
}