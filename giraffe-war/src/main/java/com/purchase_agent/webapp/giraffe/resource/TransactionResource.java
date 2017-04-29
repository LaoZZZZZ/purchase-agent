package com.purchase_agent.webapp.giraffe.resource;

import com.purchase_agent.webapp.giraffe.authentication.SecurityContextWrapper;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.persistence.TransactionDao;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import com.purchase_agent.webapp.giraffe.authentication.Roles;
import org.joda.time.DateTime;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;
/**
 * Created by lukez on 3/9/17.
 */
@Path("transactions/single/{transactionId}")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {
    private static final Logger logger = Logger.getLogger(TransactionResource.class.getName());

    private final String transactionId;
    private final Provider<DateTime> now;
    private final TransactionDao transactionDao;
    private final SecurityContextWrapper securityContextWrapper;

    @Inject
    public TransactionResource(@PathParam("transactionId") final String transactionId,
                               @RequestTime final Provider<DateTime> now,
                               final TransactionDao transactionDao,
                               @Context final SecurityContextWrapper securityContextWrapper) {
        this.transactionId = transactionId;
        this.now = now;
        this.transactionDao = transactionDao;
        this.securityContextWrapper = securityContextWrapper;
    }

    @RolesAllowed({Roles.USER, Roles.ADMIN})
    @GET
    public Response getTransaction() {
        if (!securityContextWrapper.isUserInRole(Roles.USER) && !securityContextWrapper.isUserInRole(Roles.ADMIN)) {
            logger.warning("unauthorized user when getting transactions");
            return Response.status(Response.Status.FORBIDDEN).build();
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

    // Mainly update the status or the monetary amount
    @RolesAllowed({Roles.USER, Roles.ADMIN})
    @Consumes(MediaType.APPLICATION_JSON)
    @PUT
    public Response updateTransaction(final com.purchase_agent.webapp.giraffe.mediatype.Transaction transaction) {
        if (!securityContextWrapper.isUserInRole(Roles.USER) && !securityContextWrapper.isUserInRole(Roles.ADMIN)) {
            logger.warning("unauthorized user when getting transactions");
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        final Transaction persisted = this.transactionDao.get().transactinoId(transactionId);
        // TODO(lukez): Check the user id matches the saler id in the transaction.
        if (persisted == null || persisted.isDeleted()) {
            logger.info("cant not find transaction " + this.transactionId);
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (transaction.getStatus() != null) {
                persisted.setStatus(transaction.getStatus());
            }

            if (transaction.getMoneyAmount() != null) {
                persisted.setMoneyAmount(transaction.getMoneyAmount());
            }
            ofy().save().entity(persisted).now();
            persisted.setLastModificationTime(this.now.get());
            return Response.ok().build();
        }
    }
    // Transfer the persisted transaction to medatype.
    public static com.purchase_agent.webapp.giraffe.mediatype.Transaction toWireModel(final Transaction transaction) {
        final com.purchase_agent.webapp.giraffe.mediatype.Transaction toReturn =
                new com.purchase_agent.webapp.giraffe.mediatype.Transaction();
        toReturn.setLineItemIds(transaction.getItemIds());
        toReturn.setMoneyAmount(transaction.getMoneyAmount());
        toReturn.setCreationTime(transaction.getCreationTime());
        toReturn.setLastModificationTime(transaction.getLastModificationTime());
        toReturn.setStatus(transaction.getStatus());
        toReturn.setTransactionId(transaction.getId());
        toReturn.setCustomerId(transaction.getCustomerId());
        return toReturn;
    }
}
