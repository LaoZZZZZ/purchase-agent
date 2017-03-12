package com.purchase_agent.webapp.giraffe.resource;

import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.persistence.TransactionDao;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

/**
 * Created by lukez on 3/9/17.
 */
@Path("transactions/{transaction_id}")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {
    private final DateTime now;
    private final String transactionId;
    private final TransactionDao transactionDao;

    @Inject
    public TransactionResource(@RequestTime final DateTime now,
                               @PathParam("transaction_id") final String transactionId,
                               final TransactionDao transactionDao) {
        this.now = now;
        this.transactionId = transactionId;
        this.transactionDao = transactionDao;
    }

    @GET
    public Response getTransaction() {
        if (Strings.isNullOrEmpty(transactionId)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final Transaction transaction = this.transactionDao.get().transactinoId(transactionId);

        // TODO(lukez): Check the user id matches the saler id in the transaction.
        if (transaction == null || transaction.isDeleted()) {
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
