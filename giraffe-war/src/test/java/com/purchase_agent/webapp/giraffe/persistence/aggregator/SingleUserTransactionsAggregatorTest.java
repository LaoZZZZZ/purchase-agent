package com.purchase_agent.webapp.giraffe.persistence.aggregator;

import com.purchase_agent.webapp.giraffe.aggregator.SingleUserTransactionsAggregator;
import com.purchase_agent.webapp.giraffe.mediatype.AggregatedTransactionMetrics;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import com.purchase_agent.webapp.giraffe.utils.Currency;
import com.purchase_agent.webapp.giraffe.utils.MoneyAmount;
import jdk.Exported;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Unit test for SingleUserTransactionsAggregator.
 * Created by lukez on 5/11/17.
 */
public class SingleUserTransactionsAggregatorTest {
    private static final DateTime CREATION_TIME = DateTime.parse("2016-10-15");
    private static final DateTime LAST_MODIFIED_TIME = DateTime.parse("2016-10-15");
    private static final String USERNAME = "saler_test";
    private static final Transaction.Status STATUS = Transaction.Status.PAID;
    private static final int NUM_OF_TRANSACTIONS = 4;
    private final BigDecimal AMOUNT = new BigDecimal(100);

    private List<Transaction> transactions = new ArrayList<>();
    private SingleUserTransactionsAggregator singleUserTransactionsAggregator;


    @Before
    public void setUpTest() {
        for (int i = 0; i < NUM_OF_TRANSACTIONS; i++) {
            transactions.add(createTransaction());
        }
        singleUserTransactionsAggregator = (new SingleUserTransactionsAggregator.Builder(USERNAME, CREATION_TIME))
                .transactions(transactions)
                .build();
    }

    // Test the case that all transactions are counted in each type of
    @Test
    public void test_aggregation_sameTime_success() {
        AggregatedTransactionMetrics metrics = singleUserTransactionsAggregator.aggregateTransactions();
        Assert.assertEquals("wrong money amount",
                new MoneyAmount(AMOUNT.multiply(new BigDecimal(NUM_OF_TRANSACTIONS)), Currency.USD),
                metrics.getDailyEarningInCents());
        Assert.assertEquals("wrong money amount",
                new MoneyAmount(AMOUNT.multiply(new BigDecimal(NUM_OF_TRANSACTIONS)), Currency.USD),
                metrics.getWeekLyEarningInCents());
        Assert.assertEquals("wrong money amount",
                new MoneyAmount(AMOUNT.multiply(new BigDecimal(NUM_OF_TRANSACTIONS)), Currency.USD),
                metrics.getMonthlyEarningInCents());
        Assert.assertEquals("wrong money amount",
                new MoneyAmount(AMOUNT.multiply(new BigDecimal(NUM_OF_TRANSACTIONS)), Currency.USD),
                metrics.getAnnualEarningInCents());
    }

    @Test
    public void test_aggregation_differentTime_success() {
        transactions.get(0).setLastModificationTime(CREATION_TIME.minusHours(2));
        transactions.get(1).setLastModificationTime(CREATION_TIME.minusDays(2));
        transactions.get(2).setLastModificationTime(CREATION_TIME.minusWeeks(2));
        transactions.get(3).setLastModificationTime(CREATION_TIME.minusMonths(2));
        AggregatedTransactionMetrics metrics = singleUserTransactionsAggregator.aggregateTransactions();
        Assert.assertEquals("wrong money amount",
                new MoneyAmount(AMOUNT.multiply(BigDecimal.ONE), Currency.USD),
                metrics.getDailyEarningInCents());
        Assert.assertEquals("wrong money amount",
                new MoneyAmount(AMOUNT.multiply(new BigDecimal(2)), Currency.USD),
                metrics.getWeekLyEarningInCents());
        Assert.assertEquals("wrong money amount",
                new MoneyAmount(AMOUNT.multiply(new BigDecimal(3)), Currency.USD),
                metrics.getMonthlyEarningInCents());
        Assert.assertEquals("wrong money amount",
                new MoneyAmount(AMOUNT.multiply(new BigDecimal(4)), Currency.USD),
                metrics.getAnnualEarningInCents());
    }

    @Test(expected = IllegalStateException.class)
    public void test_aggregation_mismatched_currency() {
        transactions.get(0).getMoneyAmount().setCurrency(Currency.RMB);
        singleUserTransactionsAggregator.aggregateTransactions();
    }

    private Transaction createTransaction() {
        Transaction toReturn = new Transaction();
        toReturn.setId(UUID.randomUUID().toString());
        toReturn.setStatus(STATUS);
        toReturn.setCreationTime(CREATION_TIME);
        toReturn.setLastModificationTime(LAST_MODIFIED_TIME);
        toReturn.setSaler(USERNAME);
        toReturn.setMoneyAmount(new MoneyAmount(AMOUNT, Currency.USD));
        return toReturn;
    }
}
