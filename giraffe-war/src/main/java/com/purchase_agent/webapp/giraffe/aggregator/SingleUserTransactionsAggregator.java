package com.purchase_agent.webapp.giraffe.aggregator;

import com.purchase_agent.webapp.giraffe.mediatype.AggregatedTransactionMetrics;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import com.purchase_agent.webapp.giraffe.persistence.TransactionDao;

import java.util.logging.Logger;
import java.util.List;

/**
 * Created by lukez on 5/9/17.
 */
public class SingleUserTransactionsAggregator {
    private static final Logger logger = Logger.getLogger(SingleUserTransactionsAggregator.class.getName());

    private final String username;


    private SingleUserTransactionsAggregator(final String username) {
        this.username = username;
    }

    public SingleUserTransactionsAggregator build(final String username) {
        return new SingleUserTransactionsAggregator(username);
    }

    public AggregatedTransactionMetrics aggregateTransactionMetrics(final TransactionDao transactionDao) {
        final TransactionDao.Search.Result searchResult = transactionDao.search().saler(username).execute();
        AggregatedTransactionMetrics userTransactionMetrics = new AggregatedTransactionMetrics();
        do {
            AggregatedTransactionMetrics batchMetrics = aggregateTransactions(searchResult.transactions);
            userTransactionMetrics = AggregatedTransactionMetrics.Builder.combine(userTransactionMetrics, batchMetrics);

        } while (searchResult.encodedCursor != null);
        return userTransactionMetrics;
    }

    private AggregatedTransactionMetrics aggregateTransactions(final List<Transaction> transactions) {
        AggregatedTransactionMetrics toReturn = new AggregatedTransactionMetrics();
        for (final Transaction transaction : transactions) {
            switch (transaction.getStatus()) {
                case RESERVE:
                    toReturn.getReserved().add(transaction.getId());
                    break;
                case PAID:
                    toReturn.getPaid().add(transaction.getId());
                    break;
                case SHIPPED:
                    toReturn.getShipped().add(transaction.getId());
                    break;
                case DELIVERED:
                    toReturn.getDelivered().add(transaction.getId());
                    break;
                case RETURNED:
                    toReturn.getReserved().add(transaction.getId());
                    break;
                default:
                    throw new RuntimeException("unexpected transaction status");
            }
        }
        return toReturn;
    }
}
