package com.purchase_agent.webapp.giraffe.filters;

import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.google.appengine.repackaged.com.google.common.collect.ImmutableSet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.purchase_agent.webapp.giraffe.authentication.PASecurityContext;
import com.purchase_agent.webapp.giraffe.authentication.Roles;
import com.purchase_agent.webapp.giraffe.authentication.UserAuthModel;
import com.purchase_agent.webapp.giraffe.authentication.UserAuthModelHandler;
import com.purchase_agent.webapp.giraffe.authentication.UserPrincipal;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.objectify_entity.WhiteListedUser;
import org.joda.time.DateTime;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.MultivaluedMap;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;
/**
 * Created by lukez on 2/27/17.
 */
@Priority(Priorities.AUTHENTICATION)
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {
    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());
    private static final String AUTH_HEADER = "authorization";
    private static final String USERNAME_HEADER = "username";
    private static final String EMAIL_HEADER = "emailheader";

    private final UserAuthModelHandler authModelHandler;
    private final DateTime now;

    @Inject
    public AuthenticationFilter(final UserAuthModelHandler userAuthModelHandler,
                                @RequestTime final DateTime now) {
        this.authModelHandler = userAuthModelHandler;
        this.now = now;
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) {
        final String method = requestContext.getMethod();
        final String uriPath = requestContext.getUriInfo().getPath();
        // This is the login request.
        if (method.equals("GET") && uriPath.endsWith("user/login")) {
            MultivaluedMap<String, String> queryParameters = requestContext.getUriInfo().getQueryParameters();
            List<String> username = queryParameters.get("username");
            if (username.isEmpty() || username.size() > 1) {
                logger.severe("Invalid username!");
                throw new WebApplicationException(Status.UNAUTHORIZED);
            }
            List<String> password = queryParameters.get("password");
            if (password.isEmpty() || password.size() > 1) {
                logger.severe("Invalid password string!");
                throw new WebApplicationException(Status.UNAUTHORIZED);
            }
            if (!validateUser(username.get(0), password.get(0))) {
                throw new WebApplicationException(Status.UNAUTHORIZED);
            }
            final UserAuthModel userModel = new UserAuthModel();
            userModel.setUsername(username.get(0));
            userModel.setPassword(password.get(0));
            userModel.setExpireTime(DateTime.now().plusDays(1));
            userModel.setAuthTicket(UUID.randomUUID().toString());
            final String encodedAuthToken = this.authModelHandler.encode(userModel);
            requestContext.getHeaders().add(AUTH_HEADER, encodedAuthToken);
            Principal principal = new UserPrincipal(userModel);
            requestContext.setSecurityContext(PASecurityContext.createSecurityContext(
                    ImmutableSet.of(Roles.ANY), principal, PASecurityContext.Schema.TOKEN));
            return;
        }

        String authToken = requestContext.getHeaderString(AUTH_HEADER);
        // If there is no token presented, check if the current user is white-listed
        if (Strings.isNullOrEmpty(authToken)) {
            final String email = requestContext.getHeaderString(EMAIL_HEADER);
            final String username = requestContext.getHeaderString(USERNAME_HEADER);
            if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(email)) {
                logger.severe("The user does not have auth token or username && email as white listed user!");
                throw new WebApplicationException(Status.UNAUTHORIZED);
            } else {
                final WhiteListedUser user = ofy().load().type(WhiteListedUser.class).id(username).now();
                if (user == null || user.isDeleted()) {
                    throw new WebApplicationException(Status.UNAUTHORIZED);
                }
                UserAuthModel userAuthModel = new UserAuthModel();
                userAuthModel.setUsername(username);

                Principal principal = new UserPrincipal(userAuthModel);
                requestContext.setSecurityContext(PASecurityContext.createSecurityContext(
                        ImmutableSet.copyOf(user.getRoles()), principal, PASecurityContext.Schema.WHITE_LISTED));
            }
        } else {
            logger.info("authToken:" + authToken);
            try {
                final UserAuthModel userAuthModel = this.authModelHandler.decode(authToken);
                if (Strings.isNullOrEmpty(userAuthModel.getAuthTicket())) {
                    logger.severe(String.format("got invalid auth ticket %s", userAuthModel.getAuthTicket()));
                    throw new WebApplicationException(Status.UNAUTHORIZED);
                }
                logger.info("authticket: " + userAuthModel.getAuthTicket());
                if (this.now.isAfter(userAuthModel.getExpireTime())) {
                    logger.info("The login token already expire for user " + userAuthModel.getUsername());
                    throw new WebApplicationException(Status.UNAUTHORIZED);
                }
                if (Strings.isNullOrEmpty(userAuthModel.getUsername()) || Strings.isNullOrEmpty(userAuthModel.getPassword())) {
                    logger.warning(String.format("The auth model does not have valid username %s or password %s",
                            userAuthModel.getUsername(), userAuthModel.getPassword()));
                    throw new WebApplicationException(Status.UNAUTHORIZED);
                }

                if (Strings.isNullOrEmpty(userAuthModel.getAuthTicket())) {
                    logger.warning(String.format("The auth model does not have valid auth ticket %s",
                            userAuthModel.getUsername(), userAuthModel.getPassword()));
                    throw new WebApplicationException(Status.UNAUTHORIZED);
                }
                // Replace the security context
                Principal principal = new UserPrincipal(userAuthModel);
                requestContext.setSecurityContext(PASecurityContext.createSecurityContext(
                        ImmutableSet.of(Roles.USER), principal, PASecurityContext.Schema.TOKEN));
            } catch (final Exception exp) {
                logger.severe("can not decrypt the auth token!" + authToken);
                throw new WebApplicationException(Status.UNAUTHORIZED);
            }
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
        return true;
    }
}
