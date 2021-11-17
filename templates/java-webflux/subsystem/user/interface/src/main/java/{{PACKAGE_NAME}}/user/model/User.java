package {{PACKAGE_NAME}}.user.model;

import java.util.Objects;

public final class User {

  public Integer id;
  public String familyName;
  public String givenName;

  public User(Integer id, String familyName, String givenName) {
    this.id = id;
    this.familyName = familyName;
    this.givenName = givenName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id) && Objects.equals(familyName, user.familyName) && Objects.equals(givenName, user.givenName);
  }

}
