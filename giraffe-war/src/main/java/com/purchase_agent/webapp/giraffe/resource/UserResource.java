package com.purchase_agent.webapp.giraffe.resource;

import com.google.appengine.repackaged.com.google.common.base.Preconditions;
import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;
import com.purchase_agent.webapp.giraffe.authentication.Roles;
import com.purchase_agent.webapp.giraffe.authentication.SecurityContextWrapper;
import com.purchase_agent.webapp.giraffe.authentication.UserAuthModel;
import com.purchase_agent.webapp.giraffe.filters.SensitiveInfoFilter;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.objectify_entity.User;
import com.purchase_agent.webapp.giraffe.utils.Links;
import com.purchase_agent.webapp.giraffe.utils.PasswordValidator;
import org.joda.time.DateTime;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by lukez on 2/19/17.
 */
@Path("user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private static final Logger logger = Logger.getLogger(UserResource.class.getName());

    private final PasswordValidator passwordValidator;
    private final Links links;
    private final SensitiveInfoFilter sensitiveInfoFilter;
    private final SecurityContextWrapper securityContextWrapper;
    private final Provider<DateTime> now;

    @Inject
    public UserResource(final PasswordValidator passwordValidator,
                        final Links links,
                        final SensitiveInfoFilter sensitiveInfoFilter,
                        final SecurityContextWrapper securityContextWrapper,
                        @RequestTime final Provider<DateTime> now) {
        this.passwordValidator = passwordValidator;
        this.links = links;
        this.sensitiveInfoFilter = sensitiveInfoFilter;
        this.securityContextWrapper = securityContextWrapper;
        this.now = now;
    }

    @RolesAllowed(Roles.USER)
    @Path("/login")
    @GET
    public Response getUser(@QueryParam("username") final String username,
                            @QueryParam("password") final String password) {
        if (!this.securityContextWrapper.isUserInRole(Roles.USER)) {
            logger.warning("unauthorized user " + username);
        }

        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)) {
            logger.warning("The user is null or the username is invalid!");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        com.purchase_agent.webapp.giraffe.objectify_entity.User persisted = ObjectifyService.ofy().load().key(Key.create(
                com.purchase_agent.webapp.giraffe.objectify_entity.User.class, username)).now();
        if (persisted == null) {
            logger.warning("Cant not find user " + username);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!persisted.getPassword().equals(password)) {
            logger.warning("The password does not match!");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok(this.convert(persisted)).header(this.sensitiveInfoFilter.getHeader(), "hello,world").build();
    }

    @PermitAll
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(final com.purchase_agent.webapp.giraffe.mediatype.User user) {
        final User persisted = ObjectifyService.ofy().transact(new Work<User>() {
            @Override
            public User run() {
                User persisted = ofy().load().key(Key.create(User.class, user.getUsername())).now();
                if (persisted != null) {
                    logger.warning(String.format("The user %s already exists!", user.getUsername()));
                    return null;
                }
                persisted = createPersistedUser(user);
                logger.info("Activation token: " + persisted.getActivationToken());
                ofy().save().entity(persisted).now();
                return persisted;
            }
        });
        if (persisted == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.CREATED).location(this.links.forUserCreation(
                persisted.getUsername(),persisted.getActivationToken())).build();
    }

    //TODO(lukez): remove this roles restriction.
    @RolesAllowed(Roles.USER)
    @PUT
    @Path("/activate/{username}")
    public Response activateUser(@PathParam("username") final String username,
                                 @QueryParam("activationToken") final String activationToken) {
        UserAuthModel userAuthModel = securityContextWrapper.getUserInfo();
        logger.info("User auth model: " + userAuthModel);
        if (Strings.isNullOrEmpty(activationToken)) {
            logger.warning("The activation token is null or empty!");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return ObjectifyService.ofy().transact(new Work<Response>() {
            @Override
            public Response run() {
                final com.purchase_agent.webapp.giraffe.objectify_entity.User persisted =
                        ObjectifyService.ofy().load().key(Key.create(
                                com.purchase_agent.webapp.giraffe.objectify_entity.User.class, username)).now();

                if (persisted == null) {
                    logger.warning("Could not find the user with the specified activation_token");
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

                // The activation token does not match!
                if (!activationToken.equals(persisted.getActivationToken())) {
                    logger.warning("Mismatched activation token!");
                    Response.status(Response.Status.BAD_REQUEST).build();
                }
                if (persisted.getStatus() == com.purchase_agent.webapp.giraffe.objectify_entity.User.Status.UNDER_VERIFICATION
                        || persisted.getStatus() == com.purchase_agent.webapp.giraffe.objectify_entity.User.Status.CLOSED) {
                    persisted.setStatus(com.purchase_agent.webapp.giraffe.objectify_entity.User.Status.ACTIVE);
                    ofy().save().entity(persisted).now();
                }
                return Response.status(Response.Status.NO_CONTENT).build();
            }
        });
    }

    private com.purchase_agent.webapp.giraffe.mediatype.User convert(final User persisted) {
        Preconditions.checkNotNull(persisted);
        Preconditions.checkNotNull(persisted.getStatus());
        final com.purchase_agent.webapp.giraffe.mediatype.User user = new com.purchase_agent.webapp.giraffe.mediatype.User();
        user.setUsername(persisted.getUsername());
        user.setEmail(persisted.getEmail());

        if (persisted.getStatus() == com.purchase_agent.webapp.giraffe.objectify_entity.User.Status.ACTIVE) {
            user.setStatus(com.purchase_agent.webapp.giraffe.mediatype.User.Status.ACTIVE);
        } else {
            user.setStatus(com.purchase_agent.webapp.giraffe.mediatype.User.Status.CLOSED);
        }
        user.setPhoneNumber(persisted.getPhoneNumber());
        user.setAddress(persisted.getAddress());
        return user;
    }

    private User createPersistedUser(final com.purchase_agent.webapp.giraffe.mediatype.User user) {
        Preconditions.checkNotNull(user);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(user.getUsername()));

        com.purchase_agent.webapp.giraffe.objectify_entity.User persisted =
                new com.purchase_agent.webapp.giraffe.objectify_entity.User(user.getUsername());
        persisted.setUsername(user.getUsername());
        Preconditions.checkArgument(validatePassword(user.getPassword()));
        persisted.setPassword(user.getPassword());
        Preconditions.checkArgument(!Strings.isNullOrEmpty(user.getPhoneNumber()));
        persisted.setPhoneNumber(user.getPhoneNumber());
        Preconditions.checkArgument(!Strings.isNullOrEmpty(user.getAddress()));
        persisted.setAddress(user.getAddress());
        Preconditions.checkArgument(!Strings.isNullOrEmpty(user.getEmail()));
        persisted.setEmail(user.getEmail());
        final DateTime now = DateTime.now();
        persisted.setCreationTime(now);
        persisted.setLastModifiedTime(now);
        persisted.setStatus(com.purchase_agent.webapp.giraffe.objectify_entity.User.Status.UNDER_VERIFICATION);
        persisted.setActivationToken(UUID.randomUUID().toString());
        return persisted;
    }

    private boolean validatePassword(final String password) {
        return this.passwordValidator.isValidPassword(password);
    }
}
