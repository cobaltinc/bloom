package cobalt.user;

import cobalt.post.PostProvided;
import cobalt.post.model.Post;
import cobalt.user.contract.UserWithPosts;
import cobalt.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
class UserService {

  private UserRepository userRepository;

  private PostProvided postProvided;

  UserService(UserRepository userRepository, PostProvided postProvided) {
    this.userRepository = userRepository;
    this.postProvided = postProvided;
  }

  Flux<User> findAll() {
    return this.userRepository.findAll();
  }

  Mono<UserWithPosts.Res> findById(Integer id) {
    return this.userRepository
      .findById(id)
      .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
      .flatMap(user ->
        this.postProvided
          .findByUserId(user.id)
          .collectList()
          .map(posts -> UserWithPosts.Res.from(user, posts))
      );
  }

  Flux<Post> findAllPostByUser(Integer userId) {
    return this.postProvided.findByUserId(userId);
  }

}
