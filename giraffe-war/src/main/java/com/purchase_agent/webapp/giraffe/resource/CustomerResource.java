package com.purchase_agent.webapp.giraffe.resource;

import com.googlecode.objectify.Key;
import com.purchase_agent.webapp.giraffe.authentication.Roles;
import com.purchase_agent.webapp.giraffe.objectify_entity.Customer;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Path("customers/{id}")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private static final Logger logger = Logger.getLogger(CustomerResource.class.getName());

    private final Long customerId;

    @Inject
    public CustomerResource(@PathParam("id") final Long customerId) {
        this.customerId = customerId;
    }

    @RolesAllowed({Roles.ADMIN, Roles.USER})
    @GET
    public Response getCustomer() {
        if (customerId == null) {
            logger.info("Invalid customer id!");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        final Customer persisted = ofy().load().key(Key.create(Customer.class, customerId)).now();
        if (persisted == null) {
            logger.info("Cound not find customer - " + customerId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(com.purchase_agent.webapp.giraffe.mediatype.Customer.buildFromPersistedEntity(persisted)).build();
    }

    @RolesAllowed({Roles.USER})
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCustomer(final Customer customer) {
        return Response.noContent().build();
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.USER, Roles.ADMIN})
    @DELETE
    public Response deleteCustomer(final Customer customer) {
        return Response.noContent().build();
    }
}
