package cobalt.post.model;

import java.util.Objects;

public class Post {

  public Integer id;
  public Integer userId;
  public String title;
  public String body;

  public Post(Integer id, Integer userId, String title, String body) {
    this.id = id;
    this.userId = userId;
    this.title = title;
    this.body = body;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Post post = (Post) o;
    return Objects.equals(id, post.id) && Objects.equals(userId, post.userId) && Objects.equals(title, post.title) && Objects.equals(body, post.body);
  }

}
