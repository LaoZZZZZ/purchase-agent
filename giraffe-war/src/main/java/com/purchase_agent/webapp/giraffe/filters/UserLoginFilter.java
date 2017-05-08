package com.purchase_agent.webapp.giraffe.filters;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.purchase_agent.webapp.giraffe.authentication.UserAuthModel;
import com.purchase_agent.webapp.giraffe.authentication.UserAuthModelHandler;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.objectify_entity.User;
import com.purchase_agent.webapp.giraffe.resource.UserResource;
import org.joda.time.DateTime;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by lukez on 5/6/17.
 */
@Priority(Priorities.LOGIN)
@javax.ws.rs.ext.Provider
public class UserLoginFilter implements ContainerRequestFilter {
    private static final Logger logger = Logger.getLogger(UserLoginFilter.class.getName());
    private static final String AUTH_HEADER = "authorization";

    private final UserAuthModelHandler authModelHandler;
    private final Provider<DateTime> now;
    private final ResourceInfo resourceInfo;

    @Inject
    public UserLoginFilter(final UserAuthModelHandler userAuthModelHandler,
                           @RequestTime final Provider<DateTime> now,
                           @Context final ResourceInfo resourceInfo) {
        this.authModelHandler = userAuthModelHandler;
        this.now = now;
        this.resourceInfo = resourceInfo;
    }

    @Override
    public void filter(final ContainerRequestContext containerRequestContext) {
        final String className = resourceInfo.getResourceClass().getSimpleName();
        final String methodName = resourceInfo.getResourceMethod().getName();
        // This is the login request.
        if (className.equals(UserResource.class.getSimpleName()) && methodName.equals("loginUser")) {
            MultivaluedMap<String, String> queryParameters = containerRequestContext.getUriInfo().getQueryParameters();
            List<String> username = queryParameters.get("username");
            if (username.isEmpty() || username.size() > 1) {
                logger.severe("Invalid username!");
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
            List<String> password = queryParameters.get("password");
            if (password.isEmpty() || password.size() > 1) {
                logger.severe("Invalid password string!");
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
            if (!validateUser(username.get(0), password.get(0))) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }

            // TODO(lukez): add activation code check.
            final UserAuthModel userModel = new UserAuthModel();
            userModel.setUsername(username.get(0));
            userModel.setPassword(password.get(0));
            // the token valid for one day.
            userModel.setExpireTime(this.now.get().plusDays(1));
            userModel.setAuthTicket(UUID.randomUUID().toString());
            final String encodedAuthToken = this.authModelHandler.encode(userModel);
            containerRequestContext.getHeaders().add(AUTH_HEADER, encodedAuthToken);
        }
    }

    private boolean validateUser(final String username, final String password) {
        com.purchase_agent.webapp.giraffe.objectify_entity.User persisted = ObjectifyService.ofy().load().key(Key.create(
                com.purchase_agent.webapp.giraffe.objectify_entity.User.class, username)).now();
        if (persisted == null) {
            logger.warning("Cant not find user " + username);
            return false;
        }

        if (!persisted.getPassword().equals(password)) {
            logger.warning("The password does not match!");
            return false;
        }

        if (persisted.getStatus() != User.Status.ACTIVE) {
            logger.warning(String.format("The user %s is no longer an active user", persisted.getUsername()));
            return false;
        }
        return true;
    }
}
