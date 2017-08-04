package com.purchase_agent.webapp.giraffe.aggregator.MetricsAggregator;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import com.purchase_agent.webapp.giraffe.objectify_entity.User;
import com.purchase_agent.webapp.giraffe.persistence.TransactionDao;
import com.purchase_agent.webapp.giraffe.persistence.UserDao;

import java.util.Iterator;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by lukez on 5/19/17.
 */
public class TransactionsContainer implements Iterable<TransactionsContainer.TransactionElement> {
    private final UserDao userDao;
    private final TransactionDao transactionDao;

    public static class TransactionElement {
        private final List<Transaction> transactionList;
        private final String username;

        public TransactionElement(final List<Transaction> transactionList,
                                  final String username) {
            this.transactionList = transactionList;
            this.username = username;
        }

        public List<Transaction> getTransactionList() {
            return this.transactionList;
        }

        public String getUsername() {
            return username;
        }
    }

    @Inject
    public TransactionsContainer(final UserDao userDao,
                                 final TransactionDao transactionDao) {
        this.userDao = userDao;
        this.transactionDao = transactionDao;
    }

    @Override
    public TransactionsIterator iterator() {
        return new TransactionsIterator(userDao, transactionDao);
    }

    /**
     * This class is an iterator for all transactions for all active users.
     * It is not thread-safe, synchronization is needed from client code if it is used in concurrent context.
     * Created by lukez on 5/19/17.
     */
    public class TransactionsIterator implements Iterator<TransactionElement> {

        private final UserDao userDao;
        private final TransactionDao transactionDao;
        private QueryResultIterator<User> users;

        @Inject
        public TransactionsIterator(final UserDao userDao, final TransactionDao transactionDao) {
            this.userDao = userDao;
            this.transactionDao = transactionDao;
        }

        @Override
        public boolean hasNext() {
            if (users == null) {
                initialize();
            }
            return users != null && users.hasNext();
        }

        @Override
        public TransactionElement next() {
            if (users != null && users.hasNext()) {
                final User user = users.next();
                Preconditions.checkNotNull(user, "invalid user entity!");
                Preconditions.checkState(!Strings.isNullOrEmpty(user.getUsername()));
                return new TransactionElement(getTransactionsForSingleUser(user.getUsername()), user.getUsername());
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            throw new RuntimeException("unsupported operation!");
        }

        private void initialize() {
            users = this.userDao.search().status(User.Status.ACTIVE).execute();
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
}
