package com.purchase_agent.webapp.giraffe.resource;

import com.google.common.base.Strings;
import com.purchase_agent.webapp.giraffe.aggregator.TransactionAggregator;
import com.purchase_agent.webapp.giraffe.authentication.Roles;
import com.purchase_agent.webapp.giraffe.authentication.SecurityContextWrapper;
import com.purchase_agent.webapp.giraffe.authentication.UserAuthModel;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.mediatype.AggregatedTransactionMetrics;
import org.joda.time.DateTime;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Created by lukez on 5/4/17.
 */
@Path("cron")
@Produces(MediaType.APPLICATION_JSON)
public class CronJobsResource {
    private static final Logger logger = Logger.getLogger(CronJobsResource.class.getName());

    final Provider<DateTime> now;
    final Provider<SecurityContextWrapper> securityContextWrapperProvider;
    final TransactionAggregator transactionAggregator;
    @Inject
    public CronJobsResource(@RequestTime final Provider<DateTime> now,
                            final Provider<SecurityContextWrapper> securityContextWrapperProvider,
                            final TransactionAggregator transactionAggregator) {
        this.now = now;
        this.securityContextWrapperProvider = securityContextWrapperProvider;
        this.transactionAggregator = transactionAggregator;
    }

    @RolesAllowed({Roles.CRON, Roles.ADMIN})
    @Path("transaction/summary")
    @GET
    public Response summarizeTransactions() {
        final UserAuthModel authModel = this.securityContextWrapperProvider.get().getUserInfo();
        logger.info("triggered by " + authModel.getUsername());
        final AggregatedTransactionMetrics metrics = this.transactionAggregator.aggregate();
        if (!Strings.isNullOrEmpty(authModel.getUsername())) {
            metrics.setUsername(authModel.getUsername());
        }
        logger.info("returning metrics");
        return Response.ok(metrics).build();
    }
}
