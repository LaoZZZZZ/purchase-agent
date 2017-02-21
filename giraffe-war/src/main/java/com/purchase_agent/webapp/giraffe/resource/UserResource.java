package com.purchase_agent.webapp.giraffe.resource;

import com.google.appengine.repackaged.com.google.common.base.Preconditions;
import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;
import com.purchase_agent.webapp.giraffe.mediatype.User;
import com.purchase_agent.webapp.giraffe.utils.Links;
import com.purchase_agent.webapp.giraffe.utils.PasswordValidator;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.PUT;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
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

    @Inject
    public UserResource(final PasswordValidator passwordValidator,
                        final Links links) {
        this.passwordValidator = passwordValidator;
        this.links = links;
    }

    @Path("login/")
    @GET
    public Response getUser(@QueryParam("username") final String username,
                            @QueryParam("password") final String password) {
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

        return Response.ok(this.convert(persisted)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(final User user) {
        final String activationToken = ObjectifyService.ofy().transact(new Work<String>() {
            @Override
            public String run() {
                com.purchase_agent.webapp.giraffe.objectify_entity.User persisted = ofy().load().key(Key.create(
                        com.purchase_agent.webapp.giraffe.objectify_entity.User.class, user.getUsername())).now();
                if (persisted != null) {
                    logger.warning(String.format("The user %s already exists!", user.getUsername()));
                    return null;
                }
                persisted = createPersistedUser(user);
                logger.info("Activation token: " + persisted.getActivationToken());
                ofy().save().entity(persisted).now();
                return persisted.getActivationToken();
            }
        });
        if (Strings.isNullOrEmpty(activationToken)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        logger.info("Created user location: " + this.links.forUserCreation(activationToken).toString());
        return Response.status(Response.Status.CREATED).location(this.links.forUserCreation(activationToken)).build();
    }

    @PUT
    @Path("/activate/{activation_token}")
    public Response activateUser(@PathParam("activation_token") final String activationToken) {
        if (Strings.isNullOrEmpty(activationToken)) {
            logger.warning("The activation token is null or empty!");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return ObjectifyService.ofy().transact(new Work<Response>() {
            @Override
            public Response run() {
                final List<com.purchase_agent.webapp.giraffe.objectify_entity.User> persisted = ObjectifyService.ofy().load().type(
                        com.purchase_agent.webapp.giraffe.objectify_entity.User.class)
                        .filter("activationToken", activationToken).list();

                if (persisted.isEmpty()) {
                    logger.warning("Could not find the user with the specified activation_token");
                    return Response.status(Response.Status.NOT_FOUND).build();
                }

                if (persisted.size() > 1) {
                    logger.warning("Find multiple user with the same activation token!");
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
                com.purchase_agent.webapp.giraffe.objectify_entity.User user = persisted.get(0);

                if (user.getStatus() == com.purchase_agent.webapp.giraffe.objectify_entity.User.Status.UNDER_VERIFICATION) {
                    user.setStatus(com.purchase_agent.webapp.giraffe.objectify_entity.User.Status.ACTIVE);
                    ofy().save().entity(user).now();
                }
                return Response.status(Response.Status.NO_CONTENT).build();
            }
        });
    }

    private User convert(final com.purchase_agent.webapp.giraffe.objectify_entity.User persisted) {
        Preconditions.checkNotNull(persisted);
        Preconditions.checkNotNull(persisted.getStatus());
        final User user = new User();
        user.setUsername(persisted.getUsername());
        user.setEmail(persisted.getEmail());

        if (persisted.getStatus() == com.purchase_agent.webapp.giraffe.objectify_entity.User.Status.ACTIVE) {
            user.setStatus(User.Status.ACTIVE);
        } else {
            user.setStatus(User.Status.CLOSED);
        }
        user.setPhoneNumber(persisted.getPhoneNumber());
        user.setAddress(persisted.getAddress());
        return user;
    }

    private com.purchase_agent.webapp.giraffe.objectify_entity.User createPersistedUser(final User user) {
        Preconditions.checkNotNull(user);
        com.purchase_agent.webapp.giraffe.objectify_entity.User persisted =
                new com.purchase_agent.webapp.giraffe.objectify_entity.User();

        persisted.setUserId(UUID.randomUUID().toString());
        Preconditions.checkArgument(!Strings.isNullOrEmpty(user.getUsername()));
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
