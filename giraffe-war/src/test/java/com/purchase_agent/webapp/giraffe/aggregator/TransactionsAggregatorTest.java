package com.purchase_agent.webapp.giraffe.aggregator;

import com.google.common.collect.ImmutableList;
import com.purchase_agent.webapp.giraffe.mediatype.AggregatedTransactionMetrics;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import com.purchase_agent.webapp.giraffe.objectify_entity.User;
import com.purchase_agent.webapp.giraffe.persistence.ObjectifyBaseTest;
import com.purchase_agent.webapp.giraffe.persistence.TransactionDao;
import com.purchase_agent.webapp.giraffe.persistence.UserDao;
import com.purchase_agent.webapp.giraffe.utils.Currency;
import com.purchase_agent.webapp.giraffe.utils.MoneyAmount;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Provider;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by lukez on 5/13/17.
 */
public class TransactionsAggregatorTest extends ObjectifyBaseTest {
    private static final DateTime CREATION_TIME = DateTime.parse("2016-10-15");
    private static final DateTime LAST_MODIFIED_TIME = DateTime.parse("2016-10-15");
    private static final int NUM_OF_TRANSACTIONS = 4;
    private final BigDecimal AMOUNT = new BigDecimal(100);
    private static final TransactionsContainer TRANSACTIONS_CONTAINER =
            new TransactionsContainer(new UserDao(), new TransactionDao());
    private TransactionsAggregator transactionsAggregator;

    @Before
    public void setUpTest() {
        transactionsAggregator = new TransactionsAggregator(TRANSACTIONS_CONTAINER,
                new Provider<DateTime>() {
                    @Override
                    public DateTime get() {
                        return CREATION_TIME;
                    }
                });
    }

    @Test
    public void test_aggregate_noUser() {
        List<AggregatedTransactionMetrics> aggregatedTransactionMetricsList = transactionsAggregator.aggregate();
        Assert.assertTrue("Expect empty list", aggregatedTransactionMetricsList.isEmpty());

        createUser(UUID.randomUUID().toString(), User.Status.SUSPENDED);
        aggregatedTransactionMetricsList = transactionsAggregator.aggregate();
        Assert.assertTrue("Expect empty list", aggregatedTransactionMetricsList.isEmpty());
    }

    @Test
    public void test_aggregate_singleUser() {
        final String username = UUID.randomUUID().toString();
        createUser(username, User.Status.ACTIVE);
        setTransactions(username, NUM_OF_TRANSACTIONS);
        List<AggregatedTransactionMetrics> aggregatedTransactionMetricsList = transactionsAggregator.aggregate();
        Assert.assertEquals("Expect one aggregated metircs", 1, aggregatedTransactionMetricsList.size());
        AggregatedTransactionMetrics expectedMetrics = constructAggregatedMetrics(NUM_OF_TRANSACTIONS, username, CREATION_TIME);
        Assert.assertEquals("Wrong aggregated amount", expectedMetrics, aggregatedTransactionMetricsList.get(0));
    }

    @Test
    public void test_aggregate_multipleUsers() {
        int numOfUsers = 5;
        List<User.Status> statuses = ImmutableList.of(User.Status.ACTIVE, User.Status.CLOSED, User.Status.SUSPENDED,
                User.Status.UNDER_VERIFICATION, User.Status.ACTIVE);
        Set<String> users = new HashSet<>();
        for (int i = 0; i < numOfUsers; ++i) {
            final String username = UUID.randomUUID().toString();
            createUser(username, statuses.get(i));
            users.add(username);
            setTransactions(username, NUM_OF_TRANSACTIONS);

        }
        List<AggregatedTransactionMetrics> aggregatedTransactionMetricsList = transactionsAggregator.aggregate();
        Assert.assertEquals("wrong number of user transaction metrics", 2, aggregatedTransactionMetricsList.size());

        for (AggregatedTransactionMetrics aggregatedTransactionMetrics : aggregatedTransactionMetricsList) {
            Assert.assertTrue(users.contains(aggregatedTransactionMetrics.getUsername()));
            AggregatedTransactionMetrics expectedMetrics = constructAggregatedMetrics(NUM_OF_TRANSACTIONS,
                    aggregatedTransactionMetrics.getUsername(), CREATION_TIME);
            Assert.assertEquals("Wrong aggregated amount", expectedMetrics, aggregatedTransactionMetrics);
        }
    }

    private void createUser(final String username, final User.Status status) {
        final User user = new User(username);
        user.setStatus(status);
        ofy().save().entity(user).now();
    }

    private void setTransactions(final String username, final int numOfTransaction) {
        for (int i = 0; i < numOfTransaction; ++i) {
            Transaction transaction = new Transaction();
            transaction.setId(UUID.randomUUID().toString());
            transaction.setStatus(Transaction.Status.SHIPPED);
            transaction.setCreationTime(CREATION_TIME);
            transaction.setLastModificationTime(LAST_MODIFIED_TIME);
            transaction.setSaler(username);
            transaction.setMoneyAmount(new MoneyAmount(AMOUNT, Currency.USD));
            ofy().save().entity(transaction).now();
        }
    }

    private AggregatedTransactionMetrics constructAggregatedMetrics(
            final int numOfTransactions, final String username, final DateTime creationTime) {
        AggregatedTransactionMetrics aggregatedTransactionMetrics = new AggregatedTransactionMetrics(username, creationTime);
        aggregatedTransactionMetrics.setDailyEarningInCents(
                new MoneyAmount(AMOUNT.multiply(new BigDecimal(numOfTransactions)), Currency.USD));
        aggregatedTransactionMetrics.setWeekLyEarningInCents(
                new MoneyAmount(AMOUNT.multiply(new BigDecimal(numOfTransactions)), Currency.USD));
        aggregatedTransactionMetrics.setMonthlyEarningInCents(
                new MoneyAmount(AMOUNT.multiply(new BigDecimal(numOfTransactions)), Currency.USD));
        aggregatedTransactionMetrics.setAnnualEarningInCents(
                new MoneyAmount(AMOUNT.multiply(new BigDecimal(numOfTransactions)), Currency.USD));
        return aggregatedTransactionMetrics;
    }
}
