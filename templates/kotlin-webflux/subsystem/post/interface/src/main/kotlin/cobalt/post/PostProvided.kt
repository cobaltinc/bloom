package cobalt.post

import cobalt.post.model.Post
import reactor.core.publisher.Flux

interface PostProvided {

  fun findByUserId(userId: Int): Flux<Post>

}
