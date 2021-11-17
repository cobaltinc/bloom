package {{PACKAGE_NAME}}.post

import {{PACKAGE_NAME}}.post.model.Post

interface PostProvided {

  fun findByUserId(userId: Int): List<Post>

}
