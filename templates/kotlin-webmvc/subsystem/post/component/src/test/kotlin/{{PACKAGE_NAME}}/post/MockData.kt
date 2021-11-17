package {{PACKAGE_NAME}}.post

import {{PACKAGE_NAME}}.post.model.Post

object MockData {
  val posts = listOf(
    Post(id = 1, userId = 1, title = "Lorem ipsum dolor sit amet.", body = "Curabitur ultrices lacus lorem, vitae tincidunt lorem convallis."),
    Post(id = 2, userId = 1, title = "Maecenas maximus sodales nibh vel.", body = "Pellentesque scelerisque tortor nec lorem volutpat, sed semper."),
    Post(id = 3, userId = 3, title = "Sed tempus, justo at viverra.", body = "Cras efficitur, nunc eu maximus congue, magna quam.")
  )
}