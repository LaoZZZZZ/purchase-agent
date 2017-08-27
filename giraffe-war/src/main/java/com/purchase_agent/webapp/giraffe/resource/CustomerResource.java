package com.purchase_agent.webapp.giraffe.resource;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;
import com.purchase_agent.webapp.giraffe.authentication.Roles;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.objectify_entity.Customer;
import org.joda.time.DateTime;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Provider;
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

@Path("customers/single/{id}")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private static final Logger logger = Logger.getLogger(CustomerResource.class.getName());

    private final Long customerId;
    private final Provider<DateTime> requestTime;

    @Inject
    public CustomerResource(@PathParam("id") final Long customerId,
                            @RequestTime final Provider<DateTime> requestTime) {
        this.customerId = customerId;
        this.requestTime = requestTime;
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
            logger.info("Could not find customer - " + customerId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(com.purchase_agent.webapp.giraffe.mediatype.Customer.buildFromPersistedEntity(persisted)).build();
    }

    @RolesAllowed({Roles.USER})
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCustomer(final com.purchase_agent.webapp.giraffe.mediatype.Customer customer) {
        if (customer == null) {
            logger.info("Missing payload!");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (customer.getId() != customerId) {
            logger.info(String.format("the customer (%s) in payload does not match with the id in path parameter (%s)",
                    customer.getId(), customerId));
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return ofy().transactNew(3, new Work<Response>() {
            @Override
            public Response run() {
                Customer persisted = ofy().load().key(Key.create(Customer.class, customerId)).now();
                if (persisted == null) {
                    logger.info("Could not find customer - " + customerId);
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
                persisted.setUpdateTime(requestTime.get());
                persisted.setWechat(customer.getWechat());
                persisted.setAddress(customer.getAddress());
                persisted.setPhoneNumber(customer.getPhoneNumber());
                persisted.setCustomerName(customer.getCustomerName());
                ofy().save().entity(persisted).now();
                return Response.noContent().build();
            }
        });
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({Roles.USER, Roles.ADMIN})
    @DELETE
    public Response deleteCustomer(final Customer customer) {
        return Response.noContent().build();
    }
}
