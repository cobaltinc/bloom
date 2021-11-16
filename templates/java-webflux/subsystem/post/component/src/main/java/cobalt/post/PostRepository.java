package cobalt.post;

import cobalt.post.model.Post;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Repository
class PostRepository {

  static List<Post> posts = List.of(
    new Post(1, 1, "Lorem ipsum dolor sit amet.", "Curabitur ultrices lacus lorem, vitae tincidunt lorem convallis."),
    new Post(2, 1, "Maecenas maximus sodales nibh vel.", "Pellentesque scelerisque tortor nec lorem volutpat, sed semper."),
    new Post(3, 3, "Sed tempus, justo at viverra.", "Cras efficitur, nunc eu maximus congue, magna quam."),
    new Post(4, 3, "Proin quis aliquam mauris. Proin.", "Donec id odio et mauris posuere auctor at."),
    new Post(5, 3, "Nulla placerat condimentum diam sed.", "In et lectus a arcu efficitur viverra in."),
    new Post(6, 3, "Etiam interdum facilisis urna, a.", "Phasellus euismod lectus eget libero viverra, sed rhoncus."),
    new Post(7, 4, "Curabitur quis leo feugiat, faucibus.", "Phasellus rutrum pretium auctor. Donec tincidunt ligula eros."),
    new Post(8, 5, "Praesent scelerisque gravida tortor a.", "Curabitur lobortis turpis at arcu facilisis, eu convallis."),
    new Post(9, 5, "Vivamus nibh nulla, aliquet quis.", "Suspendisse consequat feugiat nisi vel sollicitudin. Phasellus nec."),
    new Post(10, 5, "Aliquam elit erat, ornare nec.", "Phasellus finibus justo eget risus gravida, in venenatis."),
    new Post(11, 6, "Duis vitae placerat nibh. Integer.", "Sed blandit nisl eget nisl gravida scelerisque. Pellentesque."),
    new Post(12, 7, "Pellentesque eu tincidunt eros, sed.", "Nullam luctus dolor placerat lorem luctus, eu scelerisque."),
    new Post(13, 7, "Nulla tincidunt cursus consequat. Aliquam.", "Praesent lobortis bibendum est eu volutpat. Maecenas lobortis."),
    new Post(14, 7, "Etiam nec purus eget dui.", "Aenean interdum nec lacus eget elementum. Vivamus ultrices."),
    new Post(15, 7, "In non est fringilla eros.", "Quisque malesuada posuere mauris, a pellentesque diam aliquet."),
    new Post(16, 7, "Phasellus fermentum malesuada lectus nec.", "Nullam tortor magna, ullamcorper a elit a, aliquam.")
  );

  Flux<Post> findAll() {
    return Flux.fromIterable(posts);
  }

  Flux<Post> findByUserId(Integer userId) {
    return Flux.fromIterable(
      posts
        .stream()
        .filter(post -> post.userId.equals(userId))
        .collect(Collectors.toList())
    );
  }

  Mono<Post> findById(Integer id) {
    final Post post = posts
      .stream()
      .filter(it -> it.id.equals(id))
      .findFirst()
      .orElse(null);

    if (post != null) {
      return Mono.just(post);
    } else {
      return Mono.empty();
    }
  }

}
