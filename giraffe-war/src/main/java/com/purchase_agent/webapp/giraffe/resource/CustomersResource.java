package com.purchase_agent.webapp.giraffe.resource;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/customers")
public class CustomersResource {
    private final Provider<DateTime> requestTime;
    private final Links links;
    @Inject
    public CustomersResource(@RequestTime final Provider<DateTime> requestTime,
                             final Links links) {
        this.requestTime = requestTime;
        this.links = links;
    }

    @Path("/{id}")
    public Class<CustomerResource> getId() {
        return CustomerResource.class;
    }

    @RolesAllowed({Roles.USER})
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response createCustomer(final Customer customer) {
        final String customerId = UUID.randomUUID().toString();
        return Response.created(links.forCustomerCreation(customerId)).build();
    }

    @RolesAllowed({Roles.ADMIN, Roles.USER})
    @Path("search")
    @GET
    public Response getCustomers() {
        return Response.ok().build();
    }
}
