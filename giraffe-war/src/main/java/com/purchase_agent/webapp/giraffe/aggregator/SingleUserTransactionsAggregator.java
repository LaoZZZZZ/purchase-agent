package com.purchase_agent.webapp.giraffe.aggregator;

import com.google.common.base.Strings;
import com.google.common.base.Preconditions;
import com.purchase_agent.webapp.giraffe.mediatype.AggregatedTransactionMetrics;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import com.purchase_agent.webapp.giraffe.utils.MoneyAmount;
import org.joda.time.DateTime;

import java.util.logging.Logger;
import java.util.List;

/**
 * This class aggregates transactions that belong to the given user.
 * Created by lukez on 5/9/17.
 */
public class SingleUserTransactionsAggregator {
    private static final Logger logger = Logger.getLogger(SingleUserTransactionsAggregator.class.getName());

    private final String username;
    private final List<Transaction> transactionList;
    private final DateTime creationTime;

    private SingleUserTransactionsAggregator(final String username, final List<Transaction> transactionList,
                                             final DateTime creationTime) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(username), "Missing username");
        Preconditions.checkArgument(transactionList != null, "invalid transactions list");
        Preconditions.checkArgument(creationTime != null, "invalid creation time");
        this.username = username;
        this.transactionList = transactionList;
        this.creationTime = creationTime;
    }

    public AggregatedTransactionMetrics aggregateTransactions() {
        logger.info(String.format("Start aggregating metrics for user %s", username));
        AggregatedTransactionMetrics toReturn = null;
        int numOfEarnedTransaction = 0;
        for (final Transaction transaction : transactionList) {
            // skip transaction that does not belong to the current user.
            if (!transaction.getSaler().equals(username)) {
                logger.warning(String.format("Find a transaction (belongs to %s) that does not belong to %s",
                        transaction.getSaler(), username));
                continue;
            }
            switch (transaction.getStatus()) {
                case PAID:
                case SHIPPED:
                case DELIVERED:
                    if (toReturn == null) {
                        toReturn = transformToMetrics(transaction);
                    } else {
                        toReturn = AggregatedTransactionMetrics.sum(toReturn, transformToMetrics(transaction));
                    }
                    numOfEarnedTransaction += 1;
                    break;
                case RESERVE:
                case RETURNED:
                    break;
                default:
                    throw new RuntimeException("unexpected transaction status");
            }
        }
        if (numOfEarnedTransaction == 0) {
            Preconditions.checkState(toReturn == null);
            logger.info(String.format("There is no paid transaction for this user %s", username));
            toReturn = new AggregatedTransactionMetrics(username, creationTime);
        } else {
            logger.info(String.format("There are %d paid transactions", numOfEarnedTransaction));
        }
        return toReturn;
    }

    // Transform a transaction to single "aggregated" metrics
    private AggregatedTransactionMetrics transformToMetrics(final Transaction transaction) {
        AggregatedTransactionMetrics toReturn = buildAggregatedTransactionMetrics();
        toReturn.setDailyEarningInCents(getDailyMoneyAmount(transaction));
        toReturn.setWeekLyEarningInCents(getWeeklyMoneyAmount(transaction));
        toReturn.setMonthlyEarningInCents(getMonthlyMoneyAmount(transaction));
        toReturn.setAnnualEarningInCents(getAnnualMoneyAmount(transaction));
        return toReturn;
    }

    // Add username and creation time to the metrics.
    private AggregatedTransactionMetrics buildAggregatedTransactionMetrics() {
        AggregatedTransactionMetrics toReturn = new AggregatedTransactionMetrics();
        toReturn.setCreationTime(creationTime);
        toReturn.setUsername(username);
        return toReturn;
    }

    private MoneyAmount getDailyMoneyAmount(final Transaction transaction) {
        if (transaction.getLastModificationTime().isAfter(creationTime.minusDays(1))) {
            return transaction.getMoneyAmount();
        } else {
            return MoneyAmount.ZERO;
        }
    }

    private MoneyAmount getWeeklyMoneyAmount(final Transaction transaction) {
        if (transaction.getLastModificationTime().isAfter(creationTime.minusWeeks(1))) {
            return transaction.getMoneyAmount();
        } else {
            return MoneyAmount.ZERO;
        }
    }

    private MoneyAmount getMonthlyMoneyAmount(final Transaction transaction) {
        if (transaction.getLastModificationTime().isAfter(creationTime.minusMonths(1))) {
            return transaction.getMoneyAmount();
        } else {
            return MoneyAmount.ZERO;
        }
    }

    private MoneyAmount getAnnualMoneyAmount(final Transaction transaction) {
        if (transaction.getLastModificationTime().isAfter(creationTime.minusYears(1))) {
            return transaction.getMoneyAmount();
        } else {
            return MoneyAmount.ZERO;
        }
    }

    public static class Builder {
        private String username;
        private DateTime creationTime;
        private List<Transaction> transactions;

        public Builder(final String username, final DateTime creationTime) {
            this.username = username;
            this.creationTime = creationTime;
        }

        public Builder transactions(final List<Transaction> transactions) {
            this.transactions = transactions;
            return this;
        }

        public SingleUserTransactionsAggregator build() {
            return new SingleUserTransactionsAggregator(username, transactions, creationTime);
        }
    }
}
