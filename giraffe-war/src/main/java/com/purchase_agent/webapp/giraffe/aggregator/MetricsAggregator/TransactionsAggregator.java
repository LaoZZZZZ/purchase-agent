package com.purchase_agent.webapp.giraffe.aggregator.MetricsAggregator;

import com.google.common.base.Preconditions;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.mediatype.AggregatedTransactionMetrics;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import java.util.List;

/**
 * Aggregate transaction metrics for each user.
 * Created by lukez on 5/4/17.
 */
public class TransactionsAggregator {
    private static final Logger logger = Logger.getLogger(TransactionsAggregator.class.getName());

    private final TransactionsContainer transactionsContainer;
    private final Provider<DateTime> now;

    @Inject
    public TransactionsAggregator(final TransactionsContainer transactionsContainer,
                                  @RequestTime final Provider<DateTime> now) {
        this.transactionsContainer = transactionsContainer;
        this.now = now;
    }

    // Aggregate transaction for each user.
    // 1. finds all valid user.
    // 2. finds all transactions for each user.
    // 3. aggregate transactions for each user.
    public List<AggregatedTransactionMetrics> aggregate() {
        logger.info("aggregating transaction!");
        List<AggregatedTransactionMetrics> aggregatedTransactionMetricsList = new ArrayList<>();
        for(final TransactionsContainer.TransactionElement transactionElement : transactionsContainer) {
            aggregatedTransactionMetricsList.add(aggregateSingleUserMetrics(transactionElement));
        }
        return aggregatedTransactionMetricsList;
    }

    private AggregatedTransactionMetrics aggregateSingleUserMetrics(
            final TransactionsContainer.TransactionElement transactionElement) {
        Preconditions.checkNotNull(transactionElement, "invalid transaction record for single user");
        return new SingleUserTransactionsAggregator.Builder(transactionElement.getUsername(),
                now.get()).transactions(transactionElement.getTransactionList()).build().aggregateTransactions();
    }
}
