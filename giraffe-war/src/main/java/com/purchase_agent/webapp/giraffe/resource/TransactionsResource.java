package com.purchase_agent.webapp.giraffe.resource;

import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by lukez on 3/9/17.
 */
@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionsResource {
    private final DateTime now;
    @Inject
    public TransactionsResource(@RequestTime final DateTime now) {
        this.now = now;
    }

    @Path("transaction_id")
    public Class<TransactionResource> transactionResource() {return TransactionResource.class;}
    
    @Path("search")
    @GET
    public Response search() {
        // TODO(lukez): fill in the implementation.
        return Response.ok().build();
    }
}
