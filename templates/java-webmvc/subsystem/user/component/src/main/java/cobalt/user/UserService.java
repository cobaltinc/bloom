package cobalt.user;

import cobalt.post.PostProvided;
import cobalt.post.model.Post;
import cobalt.user.contract.UserWithPosts;
import cobalt.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
class UserService {

  private UserRepository userRepository;

  private PostProvided postProvided;

  UserService(UserRepository userRepository, PostProvided postProvided) {
    this.userRepository = userRepository;
    this.postProvided = postProvided;
  }

  List<User> findAll() {
    return this.userRepository.findAll();
  }

  UserWithPosts.Res findById(Integer id) {
    final User user = this.userRepository
      .findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    final List<Post> posts = this.postProvided.findByUserId(user.id);

    return UserWithPosts.Res.from(user, posts);
  }

  List<Post> findAllPostByUser(Integer userId) {
    return this.postProvided.findByUserId(userId);
  }

}
