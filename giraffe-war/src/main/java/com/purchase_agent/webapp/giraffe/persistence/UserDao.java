package com.purchase_agent.webapp.giraffe.persistence;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.googlecode.objectify.cmd.Query;
import com.purchase_agent.webapp.giraffe.objectify_entity.User;
import com.google.appengine.api.datastore.Cursor;
import com.googlecode.objectify.Key;
import org.joda.time.DateTime;

import javax.inject.Inject;

import static com.googlecode.objectify.ObjectifyService.ofy;
/**
 * Created by lukez on 2/19/17.
 */
public class UserDao {
    @Inject
    public UserDao() {
    }

    Key<User> key(final String username) {
        return com.googlecode.objectify.Key.create(User.class, username);
    }

    public Search search() {
        return new SearchImpl();
    }

    public interface Search {
        Search email(String email);
        Search phoneNumber(String phoneNumber);
        Search activationToken(String activationToken);
        Search creationTime(DateTime creationTime);
        Search status(User.Status status);
        Search next(final String next);

        QueryResultIterator<User> execute();
    }

    private static class SearchImpl implements Search {
        private Query<User> query;
        private int numResult;
        private static int CHUNK = 1000;
        private static int PAGE_SIZE = 1000;

        public SearchImpl() {
            query = ofy().load().type(User.class);
            this.numResult = PAGE_SIZE;
        }

        @Override
        public Search email(final String email) {
            if (!Strings.isNullOrEmpty(email)) {
                query = query.filter("email", email);
            }
            return this;
        }

        @Override
        public Search phoneNumber(final String phoneNumber) {
            if (!Strings.isNullOrEmpty(phoneNumber)) {
                query = query.filter("phoneNumber", phoneNumber);
            }
            return this;
        }

        @Override
        public Search activationToken(final String activationToken) {
            if (!Strings.isNullOrEmpty(activationToken)) {
                query = query.filter("activationToken", activationToken);
            }
            return this;
        }

        @Override
        public Search creationTime(final DateTime creationTime) {
            if (creationTime != null) {
                query = query.filter("creationTime", creationTime);
            }
            return this;
        }

        @Override
        public Search status(final User.Status status) {
            if (status != null) {
                query = query.filter("status", status);
            }
            return this;
        }

        @Override
        public Search next(final String next) {
            if (!Strings.isNullOrEmpty(next)) {
                query = query.startAt(Cursor.fromWebSafeString(next));
            }
            return this;
        }

        // TODO(lukez): add pagination logic.
        @Override
        public QueryResultIterator<User> execute() {
            query = this.query.chunk(CHUNK).limit(numResult + 1);
            return this.query.iterator();
        }
    }
}
