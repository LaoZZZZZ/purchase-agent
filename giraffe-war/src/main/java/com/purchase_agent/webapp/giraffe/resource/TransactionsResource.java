package com.purchase_agent.webapp.giraffe.resource;

import com.google.appengine.repackaged.com.google.common.base.Preconditions;
import com.google.appengine.repackaged.com.google.common.collect.ImmutableSet;
import com.purchase_agent.webapp.giraffe.authentication.Roles;
import com.purchase_agent.webapp.giraffe.authentication.UserAuthModel;
import com.purchase_agent.webapp.giraffe.authentication.UserPrincipal;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.mediatype.Transaction;
import com.purchase_agent.webapp.giraffe.mediatype.Transactions;
import com.purchase_agent.webapp.giraffe.persistence.TransactionDao;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.List;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by lukez on 3/9/17.
 */
@Path("/transactions/")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionsResource {
    private static final Logger logger = Logger.getLogger(TransactionsResource.class.getName());

    private final Provider<DateTime> now;
    private final SecurityContext securityContext;
    private final Links links;
    private final TransactionDao transactionDao;

    @Inject
    public TransactionsResource(@RequestTime final Provider<DateTime> now,
                                @Context final SecurityContext securityContext,
                                final Links links,
                                final TransactionDao transactionDao) {
        this.now = now;
        this.securityContext = securityContext;
        this.links = links;
        this.transactionDao = transactionDao;
    }

    @Path("single/{transactionId}")
    public Class<TransactionResource> transactionResource() {return TransactionResource.class;}

    @RolesAllowed({Roles.USER, Roles.ADMIN})
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response search(@QueryParam("customerId") final long customerId,
                           @QueryParam("saler") final String saler,
                           @QueryParam("status") final com.purchase_agent.webapp.giraffe.objectify_entity.Transaction.Status status,
                           @QueryParam("next") final String next,
                           @QueryParam("limit") final int limit,
                           @QueryParam("lastModificationTime") final long lastModificationTime) {
        if (!this.securityContext.isUserInRole(Roles.USER) && !this.securityContext.isUserInRole(Roles.ADMIN)) {
            logger.warning("Unauthorized user to create transaction!");
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        final TransactionDao.Search.Result result = transactionDao
                .search()
                .customId(customerId)
                .saler(saler)
                .status(status)
                .limit(limit)
                .next(next).execute();
        UserAuthModel userAuthModel = null;
        if (securityContext.isUserInRole(Roles.USER)) {
            userAuthModel = getUserInfo();
        }
        List<Transaction> transactionList = new ArrayList<>();
        for (final com.purchase_agent.webapp.giraffe.objectify_entity.Transaction transaction : result.transactions) {
            if (userAuthModel == null || transaction.getSaler().equals(userAuthModel.getUsername())) {
                transactionList.add(TransactionResource.toWireModel(transaction));
            }
        }
        Transactions transactions = new Transactions();
        transactions.setTransactions(transactionList);
        return Response.ok(transactions).build();
    }

    @RolesAllowed(Roles.USER)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response createTransaction(final Transaction transaction) {
        if (!this.securityContext.isUserInRole(Roles.USER)) {
            logger.warning("Unauthorized user to create transaction!");
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        final UserAuthModel authModel = getUserInfo();
        logger.info("Start to create a new transaction for user " + authModel.getUsername());
        com.purchase_agent.webapp.giraffe.objectify_entity.Transaction persisted = new
                com.purchase_agent.webapp.giraffe.objectify_entity.Transaction(UUID.randomUUID().toString());
        persisted.setCreationTime(this.now.get());
        persisted.setMoneyAmount(transaction.getMoneyAmount());
        persisted.setStatus(transaction.getStatus());
        persisted.setDeleted(false);
        persisted.setItemIds(transaction.getLineItemIds());
        persisted.setCustomerId(transaction.getCustomerId());
        persisted.setLastModificationTime(this.now.get());
        persisted.setSaler(authModel.getUsername());
        if (transaction.getLineItemIds() == null) {
            persisted.setNumOfItems(0);
        } else {
            persisted.setNumOfItems(ImmutableSet.copyOf(transaction.getLineItemIds()).size());
        }
        ofy().save().entity(persisted).now();
        logger.info("Successfully created transaction " + persisted.getId() + ".");
        return Response.created(links.forTransactionCreation(persisted.getId())).build();
    }

    private UserAuthModel getUserInfo() {
        UserPrincipal userPrincipal = (UserPrincipal) this.securityContext.getUserPrincipal();
        UserAuthModel authModel = userPrincipal.getUser();
        Preconditions.checkNotNull(authModel);
        return authModel;
    }
}
