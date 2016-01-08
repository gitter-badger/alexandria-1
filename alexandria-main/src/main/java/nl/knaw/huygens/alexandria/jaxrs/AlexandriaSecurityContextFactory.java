package nl.knaw.huygens.alexandria.jaxrs;

import static javax.ws.rs.core.SecurityContext.CLIENT_CERT_AUTH;
import static nl.knaw.huygens.alexandria.jaxrs.AlexandriaPrincipal.named;
import static nl.knaw.huygens.alexandria.jaxrs.AlexandriaRoles.ANONYMOUS;

import java.net.URI;
import java.security.Principal;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;

import nl.knaw.huygens.Log;
import nl.knaw.huygens.alexandria.config.AlexandriaConfiguration;

public class AlexandriaSecurityContextFactory {
  private final Map<String, String> keyMap; // authKey -> username

  @Inject
  public AlexandriaSecurityContextFactory(AlexandriaConfiguration config) {
    keyMap = config.getAuthKeyIndex();
  }

  public SecurityContext fromCertificate(String principalName) {
    return createContext(CLIENT_CERT_AUTH, principalName, true);
  }

  public SecurityContext fromHeaderString(String headerString) {
    if (StringUtils.isEmpty(headerString)) {
      return null;
    }

    final String[] parts = headerString.split(" ");
    final String scheme = parts[0];
    final String authKey = parts[1];
    if (!keyMap.containsKey(authKey)) {
      return null;
    }

    return createContext(scheme, keyMap.get(authKey), false);
  }

  public SecurityContext anonymous(UriInfo uriInfo) {
    return new SecurityContext() {
      @Override
      public Principal getUserPrincipal() {
        return () -> ANONYMOUS;
      }

      @Override
      public boolean isUserInRole(String role) {
        return ANONYMOUS.equals(role);
      }

      @Override
      public boolean isSecure() {
        return hasSecureScheme(uriInfo.getRequestUri());
      }

      @Override
      public String getAuthenticationScheme() {
        return null;
      }
    };
  }

  private boolean hasSecureScheme(URI requestUri) {
    return "https".equals(requestUri.getScheme());
  }

  private AlexandriaSecurityContext createContext(final String scheme, final String name, boolean isSecure) {
    setDefaultUserName(name);
    return new AlexandriaSecurityContext(scheme, named(name), isSecure);
  }

  private void setDefaultUserName(final String userName) {
    // TODO: refactor to get rid of saving user userName in a thread local.
    Log.trace("setDefaultUserName: [{}]", userName);
    ThreadContext.setUserName(userName);
  }
}
