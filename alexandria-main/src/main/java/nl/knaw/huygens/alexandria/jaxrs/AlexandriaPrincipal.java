package nl.knaw.huygens.alexandria.jaxrs;

import java.security.Principal;

import com.google.common.base.MoreObjects;

public class AlexandriaPrincipal implements Principal {
  private final String name;

  private AlexandriaPrincipal(String name) {
    this.name = name;
  }

  public static AlexandriaPrincipal named(final String name) {
    return new AlexandriaPrincipal(name);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("name", getName()).toString();
  }
}
