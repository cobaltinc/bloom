package cobalt.user.model;

public class User {
  public Integer id;
  public String familyName;
  public String givenName;

  public User(Integer id, String familyName, String givenName) {
    this.id = id;
    this.familyName = familyName;
    this.givenName = givenName;
  }
}
