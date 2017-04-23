package com.purchase_agent.webapp.giraffe.persistence;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.common.base.Strings;
import com.googlecode.objectify.cmd.Query;
import com.purchase_agent.webapp.giraffe.objectify_entity.LineItem;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;
/**
 * Created by lukez on 4/14/17.
 */
public class LineItemDao {
    @Inject
    public LineItemDao() {
    }

    public static interface Get {
        LineItem id(final String id);
        Collection<LineItem> ids(final Iterable<String> ids);
    }

    public static class GetImpl implements Get {
        public LineItem id(final String id) {
            if (!Strings.isNullOrEmpty(id)) {
                return ofy().load().type(LineItem.class).id(id).now();
            }
            return null;
        }

        public Collection<LineItem> ids(final Iterable<String> ids) {
            if(ids != null) {
                return ofy().load().type(LineItem.class).ids(ids).values();
            }
            return null;
        }
    }

    public Get get() {
        return new GetImpl();
    }

    public Search search() {
        return new SearchImpl();
    }

    public static interface Search {
        Search transactionId(final String transactionId);
        Search category(final LineItem.Category category);
        Search brand(final String brand);
        Search purchaseTime(final DateTime purchaseTime);
        Search limit(final int limit);
        Search status(final LineItem.Status status);
        Search owner(final String owner);
        Result execute();
        Search next(final String encodedCursor);

        public static class Result {
            public List<LineItem> lineItems;
            public String encodedCursor;
        }
    }

    private static class SearchImpl implements Search {
        private Query<LineItem> query;
        private int numResult;
        private static int PAGE_SIZE = 1000;

        public SearchImpl() {
            this.query = ofy().load().type(LineItem.class);
            this.numResult = PAGE_SIZE;
        }

        public Search transactionId(final String transactionId) {
            if (!Strings.isNullOrEmpty(transactionId)) {
                this.query = this.query.filter("transactionId", transactionId);
            }
            return this;
        }

        public Search category(final LineItem.Category category) {
            if (category != null) {
                this.query = this.query.filter("category", category);
            }
            return this;
        }

        public Search brand(final String brand) {
            if (!Strings.isNullOrEmpty(brand)) {
                this.query = this.query.filter("brand", brand);
            }
            return this;
        }

        public Search purchaseTime(final DateTime purchaseTime) {
            if (purchaseTime != null) {
                this.query = query.filter("purchaseTime", purchaseTime);
            }
            return this;
        }

        public Search status(final LineItem.Status status) {
            if (status != null) {
                this.query = query.filter("status", status);
            }
            return this;
        }

        public Search owner(final String owner) {
            if (!Strings.isNullOrEmpty(owner)) {
                this.query = query.filter("owner", owner);
            }
            return this;
        }

        public Search limit(final int limit) {
            numResult = limit;
            return this;
        }

        // TODO(lukez): add pagination logic.
        public Result execute() {
            query = this.query.limit(numResult + 1).chunk(numResult + 1);
            QueryResultIterator<LineItem> iteratorResult = this.query.iterator();
            int i = 0;
            List<LineItem> transactions = new ArrayList<>();
            while(iteratorResult.hasNext() && i++ < numResult) {
                transactions.add(iteratorResult.next());
            }
            Search.Result toReturn = new Search.Result();
            toReturn.lineItems = transactions;
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
