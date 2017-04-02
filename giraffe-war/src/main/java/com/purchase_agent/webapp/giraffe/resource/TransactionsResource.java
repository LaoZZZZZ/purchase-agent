package com.purchase_agent.webapp.giraffe.resource;

import com.google.appengine.repackaged.com.google.common.base.Preconditions;
import com.google.appengine.repackaged.com.google.common.collect.ImmutableSet;
import com.purchase_agent.webapp.giraffe.authentication.Roles;
import com.purchase_agent.webapp.giraffe.authentication.UserAuthModel;
import com.purchase_agent.webapp.giraffe.authentication.UserPrincipal;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.mediatype.Transaction;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.UUID;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by lukez on 3/9/17.
 */
@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionsResource {
    private final Provider<DateTime> now;
    private final SecurityContext securityContext;
    private final Links links;

    @Inject
    public TransactionsResource(@RequestTime final Provider<DateTime> now,
                                @Context final SecurityContext securityContext,
                                final Links links) {
        this.now = now;
        this.securityContext = securityContext;
        this.links = links;
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

    @RolesAllowed(Roles.USER)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response createTransaction(final Transaction transaction) {
        com.purchase_agent.webapp.giraffe.objectify_entity.Transaction persisted = new
                com.purchase_agent.webapp.giraffe.objectify_entity.Transaction(UUID.randomUUID().toString());
        persisted.setCreationTime(this.now.get());
        persisted.setMoneyAmount(transaction.getMoneyAmount());
        persisted.setStatus(transaction.getStatus());
        persisted.setDeleted(false);
        persisted.setItemIds(transaction.getLineItemIds());
        persisted.setCustomerId(transaction.getCustomerId());
        persisted.setLastModificationTime(this.now.get());
        persisted.setSaler(getUserInfo().getUsername());
        persisted.setNumOfItems(ImmutableSet.copyOf(transaction.getLineItemIds()).size());

        ofy().save().entity(persisted).now();
        return Response.created(links.forTransactionCreation(persisted.getId())).build();
    }

    private UserAuthModel getUserInfo() {
        UserPrincipal userPrincipal = (UserPrincipal) this.securityContext.getUserPrincipal();
        UserAuthModel authModel = userPrincipal.getUser();
        Preconditions.checkNotNull(authModel);
        return authModel;
    }
}
