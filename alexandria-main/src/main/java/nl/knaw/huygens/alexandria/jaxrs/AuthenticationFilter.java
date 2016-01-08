package nl.knaw.huygens.alexandria.jaxrs;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;

import nl.knaw.huygens.Log;

@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
  private static final String AUTH_HEADER = "Auth";
  private static final String CLIENT_CERT_COMMON_NAME = "x-ssl-client-s-dn-cn";

  private final AlexandriaSecurityContextFactory securityContextFactory;

  @Inject
  public AuthenticationFilter(AlexandriaSecurityContextFactory securityContextFactory) {
    this.securityContextFactory = securityContextFactory;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    checkExistingContext(requestContext);

    final SecurityContext securityContext;

    final String certName = requestContext.getHeaderString(CLIENT_CERT_COMMON_NAME);
    Log.trace("certName: [{}]", certName);
    if (!StringUtils.isEmpty(certName)) {
      securityContext = securityContextFactory.fromCertificate(certName);
    } else {
      final String headerString = requestContext.getHeaderString(AUTH_HEADER);
      Log.trace("authHeader: [{}]", headerString);
      if (!StringUtils.isEmpty(headerString)) {
        securityContext = securityContextFactory.fromHeaderString(headerString);
      } else {
        securityContext = securityContextFactory.anonymous(requestContext.getUriInfo());
      }
    }

    Log.info("Setting security context: {}", securityContext);
    requestContext.setSecurityContext(securityContext);
  }

  private void checkExistingContext(ContainerRequestContext requestContext) {
    if (Log.isWarnEnabled()) {
      final SecurityContext sc = requestContext.getSecurityContext();
      if (sc == null) {
        Log.warn("No pre-existing security context which should have been set by Jersey.");
      } else {
        Optional.ofNullable(sc.getUserPrincipal()).ifPresent(p -> Log.warn("Existing principal: [{}]", p));
      }
    }
  }
}
