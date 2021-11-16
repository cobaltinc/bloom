package cobalt.post

import cobalt.post.model.Post

interface PostProvided {

  fun findByUserId(userId: Int): List<Post>

}
