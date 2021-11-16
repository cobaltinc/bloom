package cobalt.user.contract;

import cobalt.post.model.Post;
import cobalt.user.model.User;

import java.util.List;
import java.util.Objects;

public class UserWithPosts {

  public static class Res {
    public Integer id;
    public String familyName;
    public String givenName;
    public List<Post> posts;

    Res(Integer id, String familyName, String givenName, List<Post> posts) {
      this.id = id;
      this.familyName = familyName;
      this.givenName = givenName;
      this.posts = posts;
    }

    public static Res from(User user, List<Post> posts) {
      return new Res(user.id, user.familyName, user.givenName, posts);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Res res = (Res) o;
      return Objects.equals(id, res.id) && Objects.equals(familyName, res.familyName) && Objects.equals(givenName, res.givenName) && Objects.equals(posts, res.posts);
    }
  }

}
