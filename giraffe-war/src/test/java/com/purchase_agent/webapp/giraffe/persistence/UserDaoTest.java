package com.purchase_agent.webapp.giraffe.persistence;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.purchase_agent.webapp.giraffe.objectify_entity.User;
import com.googlecode.objectify.VoidWork;
import org.joda.time.DateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.googlecode.objectify.ObjectifyService.ofy;
/**
 * Unit test for user dao.
 *
 * Created by lukez on 2/25/17.
 */
public class UserDaoTest extends ObjectifyBaseTest{
    private UserDao userDao = new UserDao();

    private static final DateTime CREATION_TIME = DateTime.parse("2016-11-11T00:00:00.000Z");
    private static final String EMAIL = "user@xxx.com";
    private static final String PHONE_NUMBER = "123456789";
    private static final String ACTIVATION_TOKEN = "dummy_activation_token";

    @Before
    public void setUpTest() {
    }

    @Test
    public void test_searchByKey_found() {
        final String username = "username_found";
        createAndSaveUser(username);
        final User user = ofy().load().key(this.userDao.key(username)).now();
        Assert.assertNotNull(user);

        Assert.assertEquals(username, user.getUsername());
        Assert.assertEquals(User.Status.ACTIVE, user.getStatus());
    }

    @Test
    public void test_searchByKey_notFound() {
        final String username = "username_not_found";
        final User user = ofy().load().key(this.userDao.key(username)).now();
        Assert.assertNull(user);
    }

    @Test
    public void test_searchByEmail_found() {
        final String username = "username_found";
        final User user = createAndSaveUser(username);
        final QueryResultIterator<User> result = this.userDao.search().email(EMAIL).execute();
        validateResult(user, result);
    }

    @Test
    public void test_searchByEmail_notFound() {
        final String username = "username_email_found";
        createAndSaveUser(username);
        final QueryResultIterator<User> result = this.userDao.search().email(EMAIL + "_not_found").execute();

        Assert.assertFalse(result.hasNext());
    }

    @Test
    public void test_searchByPhoneNumber_found() {
        final String username = "username_phone_number_found";
        final User user = createAndSaveUser(username);
        final QueryResultIterator<User> result = this.userDao.search().phoneNumber(PHONE_NUMBER).execute();
        validateResult(user, result);
    }

    @Test
    public void test_searchByStatus_found() {
        final String username = "username_status_found";
        final User user = createAndSaveUser(username);
        final QueryResultIterator<User> result = this.userDao.search().status(User.Status.ACTIVE).execute();
        validateResult(user, result);
    }

    @Test
    public void test_searchByCreationTime_found() {
        final String username = "username_creation_time_found";
        final User user = createAndSaveUser(username);
        final QueryResultIterator<User> result = this.userDao.search().creationTime(CREATION_TIME).execute();
        validateResult(user, result);
    }

    @Test
    public void test_searchByActivationToken_found() {
        final String username = "username_creation_time_found";
        final User user = createAndSaveUser(username);
        final QueryResultIterator<User> result = this.userDao.search().activationToken(ACTIVATION_TOKEN).execute();
        validateResult(user, result);
    }

    private User createAndSaveUser(final String username) {
        final User user = new User(username);
        user.setStatus(User.Status.ACTIVE);
        user.setPhoneNumber(PHONE_NUMBER);
        user.setCreationTime(CREATION_TIME);
        user.setEmail(EMAIL);
        user.setPassword("1234");
        user.setActivationToken(ACTIVATION_TOKEN);
        ObjectifyService.ofy().transact(new VoidWork() {
            public void vrun() {
                ofy().save().entity(user).now();
            }
        });
        return user;
    }

    private void validateResult(final User expected, final QueryResultIterator<User> result) {
        Assert.assertTrue(result.hasNext());
        int numOfResult = 0;
        while(result.hasNext()) {
            final User user = result.next();
            Assert.assertEquals("mismatched username", expected.getUsername(), user.getUsername());
            Assert.assertEquals("mismatched email", expected.getEmail(), user.getEmail());
            numOfResult += 1;
        }
        Assert.assertEquals("Expecting one result", 1, numOfResult);
    }
}
