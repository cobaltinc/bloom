package {{PACKAGE_NAME}}.post

import {{PACKAGE_NAME}}.post.model.Post
import org.springframework.stereotype.Repository

@Repository
internal class PostRepository {

  companion object {
    private val posts = listOf(
      Post(id = 1, userId = 1, title = "Lorem ipsum dolor sit amet.", body = "Curabitur ultrices lacus lorem, vitae tincidunt lorem convallis."),
      Post(id = 2, userId = 1, title = "Maecenas maximus sodales nibh vel.", body = "Pellentesque scelerisque tortor nec lorem volutpat, sed semper."),
      Post(id = 3, userId = 3, title = "Sed tempus, justo at viverra.", body = "Cras efficitur, nunc eu maximus congue, magna quam."),
      Post(id = 4, userId = 3, title = "Proin quis aliquam mauris. Proin.", body = "Donec id odio et mauris posuere auctor at."),
      Post(id = 5, userId = 3, title = "Nulla placerat condimentum diam sed.", body = "In et lectus a arcu efficitur viverra in."),
      Post(id = 6, userId = 3, title = "Etiam interdum facilisis urna, a.", body = "Phasellus euismod lectus eget libero viverra, sed rhoncus."),
      Post(id = 7, userId = 4, title = "Curabitur quis leo feugiat, faucibus.", body = "Phasellus rutrum pretium auctor. Donec tincidunt ligula eros."),
      Post(id = 8, userId = 5, title = "Praesent scelerisque gravida tortor a.", body = "Curabitur lobortis turpis at arcu facilisis, eu convallis."),
      Post(id = 9, userId = 5, title = "Vivamus nibh nulla, aliquet quis.", body = "Suspendisse consequat feugiat nisi vel sollicitudin. Phasellus nec."),
      Post(id = 10, userId = 5, title = "Aliquam elit erat, ornare nec.", body = "Phasellus finibus justo eget risus gravida, in venenatis."),
      Post(id = 11, userId = 6, title = "Duis vitae placerat nibh. Integer.", body = "Sed blandit nisl eget nisl gravida scelerisque. Pellentesque."),
      Post(id = 12, userId = 7, title = "Pellentesque eu tincidunt eros, sed.", body = "Nullam luctus dolor placerat lorem luctus, eu scelerisque."),
      Post(id = 13, userId = 7, title = "Nulla tincidunt cursus consequat. Aliquam.", body = "Praesent lobortis bibendum est eu volutpat. Maecenas lobortis."),
      Post(id = 14, userId = 7, title = "Etiam nec purus eget dui.", body = "Aenean interdum nec lacus eget elementum. Vivamus ultrices."),
      Post(id = 15, userId = 7, title = "In non est fringilla eros.", body = "Quisque malesuada posuere mauris, a pellentesque diam aliquet."),
      Post(id = 16, userId = 7, title = "Phasellus fermentum malesuada lectus nec.", body = "Nullam tortor magna, ullamcorper a elit a, aliquam.")
    )
  }

  fun findAll(): List<Post> = posts

  fun findByUserId(userId: Int): List<Post> = posts.filter { it.userId == userId }

  fun findById(id: Int): Post? = posts.find { it.id == id }

}
