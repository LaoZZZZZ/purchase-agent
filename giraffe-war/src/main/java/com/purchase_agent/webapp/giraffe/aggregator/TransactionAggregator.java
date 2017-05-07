package com.purchase_agent.webapp.giraffe.aggregator;

import com.purchase_agent.webapp.giraffe.metrics.AggregatedTransactionMetrics;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Created by lukez on 5/4/17.
 */
public class TransactionAggregator {
    private static final Logger logger = Logger.getLogger(TransactionAggregator.class.getName());
    @Inject
    TransactionAggregator() {
    }

    public AggregatedTransactionMetrics aggregate() {
        logger.info("aggregating transaction!");
        return new AggregatedTransactionMetrics();
    }
}
