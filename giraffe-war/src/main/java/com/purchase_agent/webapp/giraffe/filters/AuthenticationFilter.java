package com.purchase_agent.webapp.giraffe.filters;

import com.google.common.base.Strings;
import com.purchase_agent.webapp.giraffe.authentication.InternalTrafficAuthentication;
import com.purchase_agent.webapp.giraffe.authentication.TokenAuthentication;
import com.purchase_agent.webapp.giraffe.authentication.WhiteListedUserAuthentication;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import java.util.logging.Logger;

/**
 * Created by lukez on 2/27/17.
 */
@Priority(Priorities.AUTHENTICATION)
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {
    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());
    private static final String AUTH_HEADER = "authorization";

    private final TokenAuthentication tokenAuthentication;
    private final WhiteListedUserAuthentication whiteListedUserAuthentication;
    private final InternalTrafficAuthentication internalTrafficAnthentication;

    @Inject
    public AuthenticationFilter(final TokenAuthentication tokenAuthentication,
                                final WhiteListedUserAuthentication whiteListedUserAuthentication,
                                final InternalTrafficAuthentication internalTrafficAnthentication) {
        super();
        this.tokenAuthentication = tokenAuthentication;
        this.whiteListedUserAuthentication = whiteListedUserAuthentication;
        this.internalTrafficAnthentication = internalTrafficAnthentication;
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) {
        String authToken = requestContext.getHeaderString(AUTH_HEADER);
        // If there is no token presented, check if the current user is white-listed or internal traffic.
        if (Strings.isNullOrEmpty(authToken)) {
            SecurityContext securityContext = this.whiteListedUserAuthentication.authenticate(requestContext);
            if (securityContext != null) {
                requestContext.setSecurityContext(securityContext);
                return;
            }

            securityContext = this.internalTrafficAnthentication.authenticate(requestContext);
            if (securityContext != null) {
                requestContext.setSecurityContext(securityContext);
                return;
            }
            throw new WebApplicationException(Status.UNAUTHORIZED);
        } else {
            SecurityContext securityContext = this.tokenAuthentication.authenticate(requestContext);
            if (securityContext == null) {
                logger.severe("can not decrypt the auth token!" + authToken);
                throw new WebApplicationException(Status.UNAUTHORIZED);
            } else {
                requestContext.setSecurityContext(securityContext);
                return;
            }
        }
    }
}
