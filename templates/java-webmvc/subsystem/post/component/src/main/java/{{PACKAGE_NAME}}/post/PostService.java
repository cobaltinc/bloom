package {{PACKAGE_NAME}}.post;

import {{PACKAGE_NAME}}.post.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
class PostService implements PostProvided {

  private PostRepository postRepository;

  PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  List<Post> findAll() {
    return this.postRepository.findAll();
  }

  public List<Post> findByUserId(Integer userId) {
    return this.postRepository.findByUserId(userId);
  }

  Post findById(Integer id) {
    return this.postRepository
      .findById(id)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }
}
