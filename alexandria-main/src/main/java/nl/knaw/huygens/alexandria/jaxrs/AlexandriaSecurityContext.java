package nl.knaw.huygens.alexandria.jaxrs;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import com.google.common.base.MoreObjects;

import nl.knaw.huygens.Log;

public class AlexandriaSecurityContext implements SecurityContext {
  private final Principal principal;
  private final String authenticationScheme;
  private final boolean isSecure;

  protected AlexandriaSecurityContext(String authenticationScheme, Principal principal, boolean isSecure) {
    this.authenticationScheme = authenticationScheme;
    this.principal = principal;
    this.isSecure = isSecure;
  }

  @Override
  public Principal getUserPrincipal() {
    return principal;
  }

  @Override
  public boolean isUserInRole(String role) {
    Log.trace("isUserInRole({}) ?", role);
    return true;
  }

  @Override
  public boolean isSecure() {
    return isSecure;
  }

  @Override
  public String getAuthenticationScheme() {
    Log.trace("getAuthenticationScheme: {}", authenticationScheme);
    return authenticationScheme;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this) //
                      .add("scheme", getAuthenticationScheme()) //
                      .add("isSecure", isSecure()) //
                      .add("principal", getUserPrincipal()) //
                      .toString();
  }

}
