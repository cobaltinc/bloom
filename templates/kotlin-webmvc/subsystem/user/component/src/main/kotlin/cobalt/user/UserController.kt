package cobalt.user

import cobalt.post.model.Post
import cobalt.user.contract.UserWithPosts
import cobalt.user.model.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
internal class UserController(
  private val userService: UserService
) {

  @GetMapping
  fun findAllUser(): List<User> =
    this.userService.findAll()

  @GetMapping("/{id}")
  fun findAllUser(@PathVariable id: Int): UserWithPosts.Res =
    this.userService.findById(id)

  @GetMapping("/{id}/posts")
  fun findAllPostByUser(@PathVariable id: Int): List<Post> =
    this.userService.findAllPostByUser(id)

}
