package cobalt.post

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.server.ResponseStatusException

@ExtendWith(SpringExtension::class)
internal class PostServiceTest {

  @Mock
  lateinit var postRepository: PostRepository

  @InjectMocks
  lateinit var postService: PostService

  @Test
  fun findAll() {
    val posts = MockData.posts

    given(this.postRepository.findAll())
      .willReturn(posts)

    assertEquals(this.postService.findAll(), posts)
  }

  @Test
  fun findByUserId() {
    val userId = 1
    val posts = MockData.posts.filter { it.userId == userId }

    given(this.postRepository.findByUserId(userId))
      .willReturn(posts)

    assertEquals(this.postService.findByUserId(userId), posts)
  }

  @Test
  fun findById() {
    val post = MockData.posts[2]

    given(this.postRepository.findById(post.id))
      .willReturn(post)

    assertEquals(this.postService.findById(post.id), post)
  }

  @Test
  fun notFound() {
    given(this.postRepository.findById(5))
      .willReturn(null)

    assertThrows(ResponseStatusException(HttpStatus.NOT_FOUND)::class.java) { this.postService.findById(5) }
  }

}