package {{PACKAGE_NAME}}.user

import {{PACKAGE_NAME}}.post.PostProvided
import {{PACKAGE_NAME}}.post.model.Post
import {{PACKAGE_NAME}}.user.contract.UserWithPosts
import {{PACKAGE_NAME}}.user.model.User
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
internal class UserService(
  private val userRepository: UserRepository,
  private val postProvided: PostProvided
) {

  fun findAll(): List<User> =
    this.userRepository.findAll()

  fun findById(id: Int): UserWithPosts.Res {
    val user = this.userRepository.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    val posts = this.postProvided.findByUserId(user.id)
    return UserWithPosts.Res.from(user, posts)
  }

  fun findAllPostByUser(userId: Int): List<Post> =
    this.postProvided.findByUserId(userId)

}
