package com.purchase_agent.webapp.giraffe.aggregator;

import com.purchase_agent.webapp.giraffe.mediatype.AggregatedTransactionMetrics;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import com.purchase_agent.webapp.giraffe.persistence.TransactionDao;
import org.joda.time.DateTime;

import java.util.logging.Logger;
import java.util.List;

/**
 * Created by lukez on 5/9/17.
 */
public class SingleUserTransactionsAggregator {
    private static final Logger logger = Logger.getLogger(SingleUserTransactionsAggregator.class.getName());

    private final String username;
    private final List<Transaction> transactionList;
    private final DateTime creationTime;

    private SingleUserTransactionsAggregator(final String username, final List<Transaction> transactionList,
                                             final DateTime creationTime) {
        this.username = username;
        this.transactionList = transactionList;
        this.creationTime = creationTime;
    }

    public static SingleUserTransactionsAggregator build(final String username, final List<Transaction> transactionList,
                                                         final DateTime creationTime) {
        return new SingleUserTransactionsAggregator(username, transactionList, creationTime);
    }

    public AggregatedTransactionMetrics aggregateTransactions() {
        logger.info(String.format("Start aggregating metrics for user %s", username));
        AggregatedTransactionMetrics toReturn = buildAggregatedTransactionMetrics();
        int numOfEarnedTransaction = 0;
        for (final Transaction transaction : transactionList) {
            switch (transaction.getStatus()) {
                case PAID: case SHIPPED: case DELIVERED:
                    toReturn = AggregatedTransactionMetrics.sum(toReturn, transformToMetrics(transaction));
                    numOfEarnedTransaction += 1;
                    break;
                case RESERVE: case RETURNED:
                    break;
                default:
                    throw new RuntimeException("unexpected transaction status");
            }
        }
        logger.info(String.format("There are %d paid transactions", numOfEarnedTransaction));
        return toReturn;
    }

    // Transform a transaction to single "aggregated" metrics
    private AggregatedTransactionMetrics transformToMetrics(final Transaction transaction) {
        AggregatedTransactionMetrics toReturn = buildAggregatedTransactionMetrics();
        toReturn.setDailyEarningInCents(transaction.getMoneyAmount());
        toReturn.setWeekLyEarningInCents(transaction.getMoneyAmount());
        toReturn.setMonthlyEarningInCents(transaction.getMoneyAmount());
        toReturn.setAnnualEarningInCents(transaction.getMoneyAmount());
        return toReturn;
    }

    // Add username and creation time to the metrics.
    private AggregatedTransactionMetrics buildAggregatedTransactionMetrics() {
        AggregatedTransactionMetrics toReturn = new AggregatedTransactionMetrics();
        toReturn.setCreationTime(creationTime);
        toReturn.setUsername(username);
        return toReturn;
    }
}
