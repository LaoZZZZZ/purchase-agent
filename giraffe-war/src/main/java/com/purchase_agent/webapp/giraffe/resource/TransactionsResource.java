package com.purchase_agent.webapp.giraffe.resource;

import com.purchase_agent.webapp.giraffe.authentication.Roles;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import org.joda.time.DateTime;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by lukez on 3/9/17.
 */
@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionsResource {
    private final Provider<DateTime> now;
    private final SecurityContext securityContext;

    @Inject
    public TransactionsResource(@RequestTime final Provider<DateTime> now,
                                @Context final SecurityContext securityContext) {
        this.now = now;
        this.securityContext = securityContext;
    }

    @Path("/{transaction_id}")
    public Class<TransactionResource> transactionResource() {return TransactionResource.class;}

    @RolesAllowed({Roles.USER, Roles.ADMIN})
    @Path("/search")
    @GET
    public Response search() {
        // TODO(lukez): fill in the implementation.
        return Response.ok().build();
    }
}
