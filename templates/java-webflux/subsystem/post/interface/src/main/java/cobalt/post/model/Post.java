package cobalt.post.model;

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
}
