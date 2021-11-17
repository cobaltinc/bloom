package {{PACKAGE_NAME}}.user;

import {{PACKAGE_NAME}}.post.model.Post;
import {{PACKAGE_NAME}}.user.contract.UserWithPosts;
import {{PACKAGE_NAME}}.user.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
class UserController {

  private UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  Flux<User> findAllUser() {
    return this.userService.findAll();
  }

  @GetMapping("/{id}")
  Mono<UserWithPosts.Res> findUserById(@PathVariable Integer id) {
    return this.userService.findById(id);
  }

  @GetMapping("/{id}/posts")
  Flux<Post> findAllPostByUser(@PathVariable Integer id) {
    return this.userService.findAllPostByUser(id);
  }

}
