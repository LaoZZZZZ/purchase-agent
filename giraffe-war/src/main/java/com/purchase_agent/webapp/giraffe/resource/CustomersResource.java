package com.purchase_agent.webapp.giraffe.resource;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.common.base.Strings;
import com.purchase_agent.webapp.giraffe.authentication.Roles;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.objectify_entity.Customer;
import com.purchase_agent.webapp.giraffe.utils.Links;
import org.joda.time.DateTime;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Path("/customers/")
@Produces(MediaType.APPLICATION_JSON)
public class CustomersResource {
    private static final Logger logger = Logger.getLogger(CustomersResource.class.getName());

    private final Provider<DateTime> requestTime;
    private final Links links;
    @Inject
    public CustomersResource(@RequestTime final Provider<DateTime> requestTime,
                             final Links links) {
        this.requestTime = requestTime;
        this.links = links;
    }

    @Path("single/{id}")
    public Class<CustomerResource> getId() {
        return CustomerResource.class;
    }

    @RolesAllowed({Roles.USER})
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response createCustomer(final com.purchase_agent.webapp.giraffe.mediatype.Customer customer) {
        if (customer == null) {
            logger.info("The payload request is missing!");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (Strings.isNullOrEmpty(customer.getCustomerName())) {
            logger.info("The customer does not have valid name!");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        KeyRange keyRange = DatastoreServiceFactory.getDatastoreService().allocateIds("PA_CUSTOMER", 1);
        Customer persisted = new Customer(keyRange.getStart().getId());
        persisted.setAddress(customer.getAddress());
        persisted.setCustomerName(customer.getCustomerName());
        persisted.setPhoneNumber(customer.getPhoneNumber());
        persisted.setWechat(customer.getWechat());
        persisted.setCreationTime(requestTime.get());
        persisted.setUpdateTime(requestTime.get());
        ofy().save().entity(persisted).now();
        return Response.created(links.forCustomerCreation(persisted.getId())).build();
    }

    @RolesAllowed({Roles.ADMIN, Roles.USER})
    @Path("search")
    @GET
    public Response getCustomers() {
        return Response.ok().build();
    }
}
