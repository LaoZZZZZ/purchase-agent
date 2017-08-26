package com.purchase_agent.webapp.giraffe.utils;

import com.purchase_agent.webapp.giraffe.resource.CustomerResource;
import com.purchase_agent.webapp.giraffe.resource.ExpressCompanyResource;
import com.purchase_agent.webapp.giraffe.resource.LineItemResource;
import com.purchase_agent.webapp.giraffe.resource.LineItemsResource;
import com.purchase_agent.webapp.giraffe.resource.TransactionResource;
import com.purchase_agent.webapp.giraffe.resource.TransactionsResource;
import com.purchase_agent.webapp.giraffe.resource.UserResource;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Context;
import java.net.URI;
/**
 * Created by lukez on 2/20/17.
 */
@Singleton
public class Links {
    private UriInfo uriInfo;
    @Inject
    public Links(@Context UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public URI forUserCreation(final String userId, final String activationToken) {
        return this.uriInfo.getBaseUriBuilder().fromResource(UserResource.class)
                .path("/activate/{user_id}")
                .resolveTemplate("user_id", userId)
                .queryParam("activation_token", activationToken)
                .build();
    }

    public URI forLineItemCreation(final String lineItemId) {
        return this.uriInfo.getBaseUriBuilder().fromResource(LineItemResource.class)
                .resolveTemplate("lineItemId", lineItemId).build();
    }

    public URI forTransactionCreation(final String transactionId) {
        return this.uriInfo.getBaseUriBuilder().fromResource(TransactionResource.class)
                .resolveTemplate("transaction_id", transactionId)
                .build();
    }

    public URI forExpressCompany(final String companyId) {
        return this.uriInfo.getBaseUriBuilder().fromResource(ExpressCompanyResource.class)
                .resolveTemplate("companyId", companyId)
                .build();
    }

    public URI forSearchTransactions(final String next) {
        return this.uriInfo.getBaseUriBuilder().fromResource(TransactionsResource.class)
                .path("search")
                .queryParam("next", next)
                .build();
    }

    public URI forSearchLineItems(final String next) {
        return this.uriInfo.getBaseUriBuilder().fromResource(LineItemsResource.class)
                .path("search")
                .queryParam("next", next)
                .build();
    }

    public URI forCustomerCreation(final String customerId) {
        return this.uriInfo.getBaseUriBuilder().fromResource(CustomerResource.class)
                .resolveTemplate("id", customerId)
                .build();
    }
}
