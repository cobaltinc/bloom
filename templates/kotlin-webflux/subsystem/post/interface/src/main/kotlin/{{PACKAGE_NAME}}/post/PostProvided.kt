package {{PACKAGE_NAME}}.post

import {{PACKAGE_NAME}}.post.model.Post
import reactor.core.publisher.Flux

interface PostProvided {

  fun findByUserId(userId: Int): Flux<Post>

}
