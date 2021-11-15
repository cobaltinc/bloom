package cobalt.user.contract

import cobalt.post.model.Post
import cobalt.user.model.User

sealed class UserWithPosts {
  data class Res(
    val id: Int,
    val familyName: String,
    val givenName: String,
    val posts: List<Post>
  ) {
    companion object {
      fun from(user: User, posts: List<Post>) =
        Res(
          id = user.id,
          familyName = user.familyName,
          givenName = user.givenName,
          posts = posts
        )
    }
  }
}
