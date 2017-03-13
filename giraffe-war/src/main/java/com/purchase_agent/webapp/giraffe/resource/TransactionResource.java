package com.purchase_agent.webapp.giraffe.resource;

import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.persistence.TransactionDao;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import com.purchase_agent.webapp.giraffe.authentication.Roles;
import org.joda.time.DateTime;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.logging.Logger;

/**
 * Created by lukez on 3/9/17.
 */
@Path("transactions/{transaction_id}")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {
    private static final Logger logger = Logger.getLogger(TransactionResource.class.getName());

    private final String transactionId;
    private final Provider<DateTime> now;
    private final TransactionDao transactionDao;
    private final SecurityContext securityContext;

    @Inject
    public TransactionResource(@PathParam("transaction_id") final String transactionId,
                               @RequestTime final Provider<DateTime> now,
                               final TransactionDao transactionDao,
                               @Context final SecurityContext securityContext) {
        this.transactionId = transactionId;
        this.now = now;
        this.transactionDao = transactionDao;
        this.securityContext = securityContext;
    }

    @RolesAllowed({Roles.USER, Roles.ADMIN})
    @GET
    public Response getTransaction() {
        if (!securityContext.isUserInRole(Roles.USER) && !securityContext.isUserInRole(Roles.ADMIN)) {
            logger.warning("unauthorized user when getting transactions");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final Transaction transaction = this.transactionDao.get().transactinoId(transactionId);

        // TODO(lukez): Check the user id matches the saler id in the transaction.
        if (transaction == null || transaction.isDeleted()) {
            logger.info("cant not find transaction " + this.transactionId);
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(toWireModel(transaction)).build();
        }
    }

    // Transfer the persisted transaction to medatype.
    private com.purchase_agent.webapp.giraffe.mediatype.Transaction toWireModel(final Transaction transaction) {
        final com.purchase_agent.webapp.giraffe.mediatype.Transaction toReturn =
                new com.purchase_agent.webapp.giraffe.mediatype.Transaction();
        toReturn.setLineItemIds(transaction.getItemIds());
        toReturn.setMoneyAmount(transaction.getMoneyAmount());
        toReturn.setCreationTime(transaction.getCreationTime());
        toReturn.setLastModificationTime(transaction.getLastModificationTime());
        toReturn.setStatus(transaction.getStatus());
        toReturn.setTransactionId(transaction.getId());
        return toReturn;
    }
}
