package com.purchase_agent.webapp.giraffe.resource;

import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by lukez on 3/9/17.
 */
@Path("transactions/{transaction_id}")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {
    private final DateTime now;

    @Inject
    public TransactionResource(@RequestTime final DateTime now) {
        this.now = now;
    }
}
