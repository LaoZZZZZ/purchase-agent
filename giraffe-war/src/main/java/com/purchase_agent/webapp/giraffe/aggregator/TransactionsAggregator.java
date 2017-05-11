package com.purchase_agent.webapp.giraffe.aggregator;

import com.google.common.base.Strings;
import com.google.appengine.api.datastore.QueryResultIterator;

import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import com.purchase_agent.webapp.giraffe.mediatype.AggregatedTransactionMetrics;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import com.purchase_agent.webapp.giraffe.objectify_entity.User;
import com.purchase_agent.webapp.giraffe.persistence.TransactionDao;
import com.purchase_agent.webapp.giraffe.persistence.UserDao;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.joda.time.DateTime;

import java.util.List;
/**
 * Created by lukez on 5/4/17.
 */
public class TransactionsAggregator {
    private static final Logger logger = Logger.getLogger(TransactionsAggregator.class.getName());

    private final UserDao userDao;
    private final TransactionDao transactionDao;
    private final Provider<DateTime> now;

    @Inject
    TransactionsAggregator(final UserDao userDao, final TransactionDao transactionDao,
                           @RequestTime final Provider<DateTime> now) {
        this.userDao = userDao;
        this.transactionDao = transactionDao;
        this.now = now;
    }

    public List<AggregatedTransactionMetrics> aggregate() {
        logger.info("aggregating transaction!");
        final QueryResultIterator<User> users = this.userDao.search().status(User.Status.ACTIVE).execute();
        List<AggregatedTransactionMetrics> aggregatedTransactionMetricsList = new ArrayList<>();
        while(users.hasNext()) {
            final User user = users.next();
            List<Transaction> transactionList = getTransactionsForSingleUser(user.getUsername());
            aggregatedTransactionMetricsList.add(SingleUserTransactionsAggregator.build(user.getUsername(),
                    transactionList, now.get()).aggregateTransactions());
        }
        return aggregatedTransactionMetricsList;
    }

    private List<Transaction> getTransactionsForSingleUser(final String username) {
        TransactionDao.Search.Result searchResult = transactionDao.search().saler(username).execute();
        List<Transaction> toReturn = new ArrayList<>();
        do {
            toReturn.addAll(searchResult.transactions);
            searchResult = transactionDao.search().saler(username).next(searchResult.encodedCursor).execute();
        } while (!Strings.isNullOrEmpty(searchResult.encodedCursor));
        return toReturn;
    }
}
