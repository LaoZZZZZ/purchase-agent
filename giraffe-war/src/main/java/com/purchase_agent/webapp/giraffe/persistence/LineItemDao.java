package com.purchase_agent.webapp.giraffe.persistence;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.common.base.Strings;
import com.googlecode.objectify.cmd.Query;
import com.purchase_agent.webapp.giraffe.objectify_entity.LineItem;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.Collection;
import static com.googlecode.objectify.ObjectifyService.ofy;
/**
 * Created by lukez on 4/14/17.
 */
public class LineItemDao {
    @Inject
    LineItemDao() {
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

    Search search() {
        return new SearchImpl();
    }

    private static interface Search {
        Search transactionId(final String transactionId);
        Search category(final LineItem.Category category);
        Search brand(final String brand);
        Search purchaseTime(final DateTime purchaseTime);
        Search status(final LineItem.Status status);
        QueryResultIterator<LineItem> execute();
    }

    private static class SearchImpl implements Search {
        private Query<LineItem> query;
        private int numResult;
        private static int CHUNK = 1000;
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

        // TODO(lukez): add pagination logic.
        public QueryResultIterator<LineItem> execute() {
            query = this.query.chunk(CHUNK).limit(numResult + 1);
            return this.query.iterator();
        }
    }
}
