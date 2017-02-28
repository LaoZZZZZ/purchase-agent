package com.purchase_agent.webapp.giraffe.filters;

import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.purchase_agent.webapp.giraffe.authentication.UserAuthModel;
import com.purchase_agent.webapp.giraffe.authentication.UserAuthModelHandler;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.Priorities;
/**
 * Created by lukez on 2/27/17.
 */
@Priority(Priorities.AUTHENTICATION)
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {
    private static final String AUTH_HEADER = "authorization";

    private final UserAuthModelHandler authModelHandler;

    @Inject
    public AuthenticationFilter(final UserAuthModelHandler userAuthModelHandler) {
        this.authModelHandler = userAuthModelHandler;
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) {
        final String method = requestContext.getMethod();
        final String uriPath = requestContext.getUriInfo().getPath();
        // This is the login request.
        if (method.equals("GET") && uriPath.endsWith("user/login")) {
            return;
        }

        String authToken = requestContext.getHeaderString(AUTH_HEADER);
        if (Strings.isNullOrEmpty(authToken)) {
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }
        try {
            final UserAuthModel userAuthModel = this.authModelHandler.decode(authToken);
            // TODO(lukez): add customized security context
        } catch (final Exception exp) {
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }
    }
}
