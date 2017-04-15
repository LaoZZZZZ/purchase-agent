package com.purchase_agent.webapp.giraffe.persistence;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.common.base.Strings;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * DAO class for transaction entity.
 * Created by lukez on 3/6/17.
 * TODO(lukez): add unit test for this class.
 */
public class TransactionDao {
    @Inject
    public TransactionDao() {
    }

    public Get get() {
        return new GetImpl();
    }
    public interface Get {
        Transaction transactinoId(String transactionId);
        Collection<Transaction> transactionIds(Iterable<String> transactionIds);
    }

    private static class GetImpl implements Get {
        @Override
        public Transaction transactinoId(final String transactionId) {
            return ofy().load().key(key(transactionId)).now();
        }
        @Override
        public Collection<Transaction> transactionIds(Iterable<String> transactionIds) {
            return ofy().load().type(Transaction.class).ids(transactionIds).values();
        }
    }

    public static Key<Transaction> key(final String transactionId) {
        return Key.create(Transaction.class, transactionId);
    }

    public Search search() {
        return new SearchImpl();
    }

    public static interface Search {
        Search customId(final long customId);
        Search lastModificationTime(final DateTime lastModified);
        Search saler(final String salerId);
        Search status(final Transaction.Status status);
        Search isDeleted(final boolean deleted);
        Search next(final String encodedCursor);
        Search limit(final int limit);
        Result execute();

        public static class Result {
            public List<Transaction> transactions;
            public String encodedCursor;
        }
    }

    private static class SearchImpl implements Search {
        private Query<Transaction> query;
        private int numResult;

        private static int CHUNK = 1000;
        private static int PAGE_SIZE = 1000;

        public SearchImpl() {
            this.query = ofy().load().type(Transaction.class);
            numResult = 1000;
        }

        public Search customId(final long customId) {
            this.query = this.query.filter("customerId", customId);
            return this;
        }

        public Search lastModificationTime(final DateTime lastModified) {
            if (lastModified != null) {
                this.query = this.query.filter("lastModificationTime", lastModified);
            }
            return this;
        }

        public Search saler(final String salerId) {
            if (!Strings.isNullOrEmpty(salerId)) {
                this.query = this.query.filter("saler", salerId);
            }
            return this;
        }

        public Search status(final Transaction.Status status) {
            if (status != null) {
                this.query = query.filter("status", status);
            }
            return this;
        }

        public Search isDeleted(final boolean deleted) {
            this.query = query.filter("isDeleted", deleted);
            return this;
        }

        public Search limit(final int limit) {
            numResult = limit;
            return this;
        }

        // TODO(lukez): add pagination logic.
        public Result execute() {
            query = this.query.limit(numResult + 1).chunk(numResult + 1);
            QueryResultIterator<Transaction> iteratorResult = this.query.iterator();
            int i = 0;
            List<Transaction> transactions = new ArrayList<>();
            while(iteratorResult.hasNext() && i++ < numResult) {
                transactions.add(iteratorResult.next());
            }
            Result toReturn = new Result();
            toReturn.transactions = transactions;
            if (iteratorResult.hasNext()) {
                toReturn.encodedCursor = iteratorResult.getCursor().toWebSafeString();
            }
            return toReturn;
        }

        public Search next(final String encodedCursor) {
            if (!Strings.isNullOrEmpty(encodedCursor))
                query = this.query.startAt(Cursor.fromWebSafeString(encodedCursor));
            return this;
        }
    }
}
