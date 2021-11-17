package {{PACKAGE_NAME}}.post

import {{PACKAGE_NAME}}.post.model.Post
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
internal class PostService(
  private val postRepository: PostRepository
): PostProvided {

  fun findAll(): List<Post> =
    this.postRepository.findAll()

  override fun findByUserId(userId: Int): List<Post> =
    this.postRepository.findByUserId(userId)

  fun findById(id: Int): Post =
    this.postRepository.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

}
