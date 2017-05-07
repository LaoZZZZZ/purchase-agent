package com.purchase_agent.webapp.giraffe.authentication;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Authenticate token user.
 * Created by lukez on 5/6/17.
 */
public class TokenAuthentication {
    private static final Logger logger = Logger.getLogger(TokenAuthentication.class.getName());
    private static final String AUTH_HEADER = "authorization";

    private final UserAuthModelHandler authModelHandler;
    private final Provider<DateTime> now;

    @Inject
    public TokenAuthentication(final UserAuthModelHandler userAuthModelHandler,
                               @RequestTime final javax.inject.Provider<DateTime> now) {
        this.authModelHandler = userAuthModelHandler;
        this.now = now;
    }

    public SecurityContext authenticate(final ContainerRequestContext containerRequestContext) {
        String authToken = containerRequestContext.getHeaderString(AUTH_HEADER);
        if (!Strings.isNullOrEmpty(authToken)) {
            try {
                logger.info("authToken:" + authToken);
                final UserAuthModel userAuthModel = this.authModelHandler.decode(authToken);
                if (Strings.isNullOrEmpty(userAuthModel.getAuthTicket())) {
                    logger.severe(String.format("got invalid auth ticket %s", userAuthModel.getAuthTicket()));
                    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                }
                logger.info("authticket: " + userAuthModel.getAuthTicket());
                if (this.now.get().isAfter(userAuthModel.getExpireTime())) {
                    logger.info("The login token already expire for user " + userAuthModel.getUsername());
                    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                }
                if (Strings.isNullOrEmpty(userAuthModel.getUsername()) || Strings.isNullOrEmpty(userAuthModel.getPassword())) {
                    logger.warning(String.format("The auth model does not have valid username %s or password %s",
                            userAuthModel.getUsername(), userAuthModel.getPassword()));
                    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                }

                if (Strings.isNullOrEmpty(userAuthModel.getAuthTicket())) {
                    logger.warning(String.format("The auth model does not have valid auth ticket %s",
                            userAuthModel.getUsername(), userAuthModel.getPassword()));
                    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                }
                // Replace the security context
                Principal principal = new UserPrincipal(userAuthModel);
                logger.info("The user has valid token!");
                return PASecurityContext.createSecurityContext(
                        ImmutableSet.of(Roles.USER), principal, PASecurityContext.Schema.TOKEN);
            } catch (final Exception exp) {
                logger.log(Level.SEVERE, "invalid token", exp);
                return null;
            }
        }
        return null;
    }
}
