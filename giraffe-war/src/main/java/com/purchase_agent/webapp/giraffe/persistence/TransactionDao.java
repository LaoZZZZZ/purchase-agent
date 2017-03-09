package com.purchase_agent.webapp.giraffe.persistence;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import org.joda.time.DateTime;

import java.util.Collection;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * DAO class for transaction entity.
 * Created by lukez on 3/6/17.
 * TODO(lukez): add unit test for this class.
 */
public class TransactionDao {
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

    Search search() {
        return new SearchImpl();
    }

    private static interface Search {
        Search customId(final String customId);
        Search lastModificationTime(final DateTime lastModified);
        Search saler(final String salerId);
        Search itemId(final String itemId);
        Search status(final Transaction.Status status);
        QueryResultIterator<Transaction> execute();
    }

    private static class SearchImpl implements Search {
        private Query<Transaction> query;
        private int numResult;
        private static int CHUNK = 1000;
        private static int PAGE_SIZE = 1000;

        public SearchImpl() {
            this.query = ofy().load().type(Transaction.class);
            this.numResult = PAGE_SIZE;
        }

        public Search customId(final String customId) {
            if (!Strings.isNullOrEmpty(customId)) {
                this.query = this.query.filter("customerId", customId);
            }
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

        public Search itemId(final String itemId) {
            if (!Strings.isNullOrEmpty(itemId)) {
                this.query = query.filter("itemId", itemId);
            }
            return this;
        }

        public Search status(final Transaction.Status status) {
            if (status != null) {
                this.query = query.filter("status", status);
            }
            return this;
        }

        // TODO(lukez): add pagination logic.
        public QueryResultIterator<Transaction> execute() {
            query = this.query.chunk(CHUNK).limit(numResult + 1);
            return this.query.iterator();
        }
    }
}
