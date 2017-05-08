package com.purchase_agent.webapp.giraffe.filters;

import com.google.common.base.Strings;
import com.purchase_agent.webapp.giraffe.authentication.InternalTrafficAuthentication;
import com.purchase_agent.webapp.giraffe.authentication.TokenAuthentication;
import com.purchase_agent.webapp.giraffe.authentication.WhiteListedUserAuthentication;
import com.purchase_agent.webapp.giraffe.resource.UserResource;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
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
    private final ResourceInfo resourceInfo;

    @Inject
    public AuthenticationFilter(final TokenAuthentication tokenAuthentication,
                                final WhiteListedUserAuthentication whiteListedUserAuthentication,
                                final InternalTrafficAuthentication internalTrafficAnthentication,
                                @Context final ResourceInfo resourceInfo) {
        super();
        this.tokenAuthentication = tokenAuthentication;
        this.whiteListedUserAuthentication = whiteListedUserAuthentication;
        this.internalTrafficAnthentication = internalTrafficAnthentication;
        this.resourceInfo = resourceInfo;
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) {
        final String method = resourceInfo.getResourceMethod().getName();
        final String resourceName = resourceInfo.getResourceClass().getSimpleName();
        // create user endpoint
        if (resourceName.equals(UserResource.class.getSimpleName()) &&
                (method.equals("createUser") || method.equals("activateUser"))) {
            return;
        }
        String authToken = requestContext.getHeaderString(AUTH_HEADER);
        // If there is no token presented, check if the current user is white-listed
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
