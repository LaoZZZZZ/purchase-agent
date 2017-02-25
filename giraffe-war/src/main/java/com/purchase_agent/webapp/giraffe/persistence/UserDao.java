package com.purchase_agent.webapp.giraffe.persistence;

import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.googlecode.objectify.cmd.Query;
import com.purchase_agent.webapp.giraffe.objectify_entity.User;

import org.joda.time.DateTime;

import static com.googlecode.objectify.ObjectifyService.ofy;
/**
 * Created by lukez on 2/19/17.
 */
public class UserDao {

    public Search search() {
        return new SearchImpl();
    }

    private static interface Search {
        Search email(String email);
        Search phoneNumber(String phoneNumber);
        Search activationToken(String activationToken);
        Search creationTime(DateTime creationTime);
        Search status(User.Status status);
        Query<User> execute();
    }

    private static class SearchImpl implements Search {
        private Query<User> query;
        private int numResult;
        private static int CHUNK = 1000;

        @Override
        public Search email(final String email) {
            if (!Strings.isNullOrEmpty(email)) {
                query = ofy().load().type(User.class).filter("email", email);
            }
            return this;
        }

        @Override
        public Search phoneNumber(final String phoneNumber) {
            if (!Strings.isNullOrEmpty(phoneNumber)) {
                query = ofy().load().type(User.class).filter("phoneNumber", phoneNumber);
            }
            return this;
        }

        @Override
        public Search activationToken(final String activationToken) {
            if (!Strings.isNullOrEmpty(activationToken)) {
                query = ofy().load().type(User.class).filter("activationToken", activationToken);
            }
            return this;
        }

        @Override
        public Search creationTime(final DateTime creationTime) {
            if (creationTime != null) {
                query = ofy().load().type(User.class).filter("creationTime", creationTime);
            }
            return this;
        }

        @Override
        public Search status(final User.Status status) {
            if (status != null) {
                query = ofy().load().type(User.class).filter("status", status);
            }
            return this;
        }

        @Override
        public Query<User> execute() {
            Query<User> result = this.query.chunk(CHUNK).limit(numResult + 1);
            query = query.
            return this.query;
        }
    }
}
