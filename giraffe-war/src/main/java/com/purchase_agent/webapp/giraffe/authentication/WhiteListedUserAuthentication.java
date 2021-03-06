package com.purchase_agent.webapp.giraffe.authentication;

import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.google.appengine.repackaged.com.google.common.collect.ImmutableSet;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.objectify_entity.WhiteListedUser;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Authenticate white listed emails.
 * Return a valid security context if it is authorize otherwise it returns null
 * Created by lukez on 5/6/17.
 */
public class WhiteListedUserAuthentication {
    private final static Logger logger = Logger.getLogger(WhiteListedUserAuthentication.class.getName());
    private static final String USERNAME_HEADER = "username";
    private static final String EMAIL_HEADER = "emailheader";

    private final Provider<DateTime> requestTime;

    @Inject
    public WhiteListedUserAuthentication(@RequestTime Provider<DateTime> requestTime) {
        this.requestTime = requestTime;
    }

    public SecurityContext authenticate(final ContainerRequestContext requestContext) {
        final String email = requestContext.getHeaderString(EMAIL_HEADER);
        final String username = requestContext.getHeaderString(USERNAME_HEADER);
        if (!Strings.isNullOrEmpty(username) && !Strings.isNullOrEmpty(email)) {
            final WhiteListedUser user = ofy().load().type(WhiteListedUser.class).id(username).now();
            if (user == null || user.isDeleted()) {
                logger.info(String.format("user %s does not exist or has already been deleted", user));
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }

            if (requestTime.get().isAfter(user.getExpirationTime())) {
                logger.info(String.format("The white listed user (%s & %s) already expires", user.getUserName(),
                        user.getExpirationTime()));
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            }
            UserAuthModel userAuthModel = new UserAuthModel();
            userAuthModel.setUsername(username);
            userAuthModel.setExpireTime(user.getExpirationTime());

            Principal principal = new UserPrincipal(userAuthModel);
            logger.info("The user is white listed");
            return PASecurityContext.createSecurityContext(
                    ImmutableSet.copyOf(user.getRoles()), principal, PASecurityContext.Schema.WHITE_LISTED);
        }
        return null;
    }
}
