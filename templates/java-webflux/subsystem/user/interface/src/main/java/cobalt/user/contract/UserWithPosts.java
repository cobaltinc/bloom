package cobalt.user.contract;

import cobalt.post.model.Post;
import cobalt.user.model.User;

import java.util.List;

public class UserWithPosts {

  public static class Res {
    Integer id;
    String familyName;
    String givenName;
    List<Post> posts;

    Res(Integer id, String familyName, String givenName, List<Post> posts) {
      this.id = id;
      this.familyName = familyName;
      this.givenName = givenName;
      this.posts = posts;
    }

    public static Res from(User user, List<Post> posts) {
      return new Res(user.id, user.familyName, user.givenName, posts);
    }
  }

}
