package com.purchase_agent.webapp.giraffe.resource;

import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.purchase_agent.webapp.giraffe.authentication.Roles;
import com.purchase_agent.webapp.giraffe.authentication.SecurityContextWrapper;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.objectify_entity.User;
import com.purchase_agent.webapp.giraffe.objectify_entity.WhiteListedUser;
import com.purchase_agent.webapp.giraffe.persistence.UserDao;
import org.joda.time.DateTime;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.inject.Provider;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by lukez on 5/7/17.
 */
@Path("admin")
@Produces(MediaType.APPLICATION_JSON)
public class AdministratorResource {
    private static final Logger logger = Logger.getLogger(AdministratorResource.class.getName());

    private final Provider<DateTime> requestTime;
    private final Provider<SecurityContextWrapper> securityContextWrapperProvider;
    private final UserDao userDao;

    @Inject
    public AdministratorResource(@RequestTime final Provider<DateTime> requestTime,
                                 final Provider<SecurityContextWrapper> securityContextWrapperProvider,
                                 final UserDao userDao) {
        this.requestTime = requestTime;
        this.securityContextWrapperProvider = securityContextWrapperProvider;
        this.userDao = userDao;
    }

    // Add white listed member
    @RolesAllowed({Roles.USER, Roles.ADMIN})
    @Path("add_member")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMember(final com.purchase_agent.webapp.giraffe.mediatype.WhiteListedUser mediaWhiteListed) {
        if (mediaWhiteListed == null) {
            logger.info("invalid white listed request body!");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (Strings.isNullOrEmpty(mediaWhiteListed.getUsername())) {
            logger.info("user name is empty or null");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (mediaWhiteListed.getRoles() == null || mediaWhiteListed.getRoles().isEmpty()) {
            logger.info("roels are empty or null");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final User user = this.userDao.get(mediaWhiteListed.getUsername());
        if (user == null) {
            logger.info("Could not find user " + mediaWhiteListed.getUsername());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        WhiteListedUser whiteListedUser = new WhiteListedUser(mediaWhiteListed.getUsername());
        whiteListedUser.setDeleted(false);
        whiteListedUser.setCreatedTime(requestTime.get());
        whiteListedUser.setExpirationTime(mediaWhiteListed.getExpirationTime());
        whiteListedUser.setRoles(mediaWhiteListed.getRoles());
        whiteListedUser.setUserEmail(mediaWhiteListed.getUserEmail());
        ofy().save().entity(whiteListedUser).now();
        return Response.ok().build();
    }
}
