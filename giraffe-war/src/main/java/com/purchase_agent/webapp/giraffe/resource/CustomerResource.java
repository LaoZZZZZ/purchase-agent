package com.purchase_agent.webapp.giraffe.resource;

import com.purchase_agent.webapp.giraffe.authentication.Roles;
import com.purchase_agent.webapp.giraffe.objectify_entity.Customer;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("customers/{id}")
public class CustomerResource {
    private final String customerId;

    public CustomerResource(@PathParam("id") final String customerId) {
        this.customerId = customerId;
    }

    @RolesAllowed({Roles.ADMIN, Roles.USER})
    public Response getCustomer() {
        return Response.ok().build();
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
