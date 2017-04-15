package com.purchase_agent.webapp.giraffe.persistence.persistence;

import com.google.common.collect.ImmutableList;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import com.purchase_agent.webapp.giraffe.persistence.ObjectifyBaseTest;
import com.purchase_agent.webapp.giraffe.persistence.TransactionDao;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import static com.googlecode.objectify.ObjectifyService.ofy;
/**
 *
 * Created by lukez on 4/11/17.
 */
public class TransactionDaoTest extends ObjectifyBaseTest {
    private TransactionDao transactionDao = new TransactionDao();

    private static final DateTime CREATION_TIME = DateTime.parse("2016-10-15");
    private static final DateTime LAST_MODIFIED_TIME = DateTime.parse("2016-10-15");
    private static final String SALER = "saler_test";
    private static final Transaction.Status STATUS = Transaction.Status.PAID;
    private static final boolean IS_DELETED = false;
    private static final long CUSTOMER_ID = 123L;
    private static final List<String> ITEM_IDS = ImmutableList.of("item_1", "item_2", "item_3");

//    @Test
//    public void test_get_success() {
//        Transaction created = createAndPersistTransaction();
//        Transaction retrieved = this.transactionDao.get().transactinoId(created.getId());
//        validateResult(created, retrieved);
//    }
//
//    @Test
//    public void test_get_notFound() {
//        Transaction retrieved = this.transactionDao.get().transactinoId("not_found");
//        Assert.assertNull(retrieved);
//    }
//
//    @Test
//    public void test_search_success() {
//        Transaction created = createAndPersistTransaction();
//
//        // search by modification time
//        QueryResultIterator<Transaction> retrieved =
//                this.transactionDao.search().lastModificationTime(created.getLastModificationTime()).execute();
//        Assert.assertTrue("Expecting one result", retrieved.hasNext());
//        while(retrieved.hasNext()) {
//            validateResult(created, retrieved.next());
//        }
//
//        // search by saler
//        retrieved = this.transactionDao.search().saler(created.getSaler()).execute();
//        Assert.assertTrue("Expecting one result", retrieved.hasNext());
//        while(retrieved.hasNext()) {
//            validateResult(created, retrieved.next());
//        }
//
//        //search by customer id
//        retrieved = this.transactionDao.search().customId(created.getCustomerId()).execute();
//        Assert.assertTrue("Expecting one result", retrieved.hasNext());
//        while(retrieved.hasNext()) {
//            validateResult(created, retrieved.next());
//        }
//
//        // search by is deleted
//        retrieved = this.transactionDao.search().isDeleted(created.isDeleted()).execute();
//        Assert.assertTrue("Expecting one result", retrieved.hasNext());
//        while(retrieved.hasNext()) {
//            validateResult(created, retrieved.next());
//        }
//
//        // search by status
//        retrieved = this.transactionDao.search().status(created.getStatus()).execute();
//        Assert.assertTrue("Expecting one result", retrieved.hasNext());
//        while(retrieved.hasNext()) {
//            validateResult(created, retrieved.next());
//        }
//
//    }

    @Test
    public void test_search_pagination_success() {
        // create three transaction and set the pagination size to be 2.
        Map<String,Transaction> created = new HashMap<>();
        for (int i = 0; i < 4; ++i) {
            final Transaction transaction = createAndPersistTransaction();
            created.put(transaction.getId(), transaction);
        }
        TransactionDao.Search search = transactionDao.search().saler(SALER).limit(3);

        TransactionDao.Search.Result result = search.execute();
        // get all result.
        List<Transaction> retrievedTransactions = new ArrayList<>();
        while(true) {
            retrievedTransactions.addAll(result.transactions);
            if (result.encodedCursor != null) {
                search = search.next(result.encodedCursor);
                result = search.execute();
            } else {
                break;
            }
        }
        Assert.assertEquals("wrong query result", created.size(), retrievedTransactions.size());
        for(final Transaction transaction : retrievedTransactions) {
            Assert.assertTrue("missing transaction", created.containsKey(transaction.getId()));
        }
    }

    private Transaction createAndPersistTransaction() {
        Transaction toReturn = new Transaction();
        toReturn.setId(UUID.randomUUID().toString());
        toReturn.setStatus(STATUS);
        toReturn.setCreationTime(CREATION_TIME);
        toReturn.setLastModificationTime(LAST_MODIFIED_TIME);
        toReturn.setDeleted(IS_DELETED);
        toReturn.setSaler(SALER);
        toReturn.setCustomerId(CUSTOMER_ID);
        toReturn.setItemIds(ITEM_IDS);
        toReturn.setNumOfItems(ITEM_IDS.size());
        ofy().save().entity(toReturn).now();
        return toReturn;
    }

    private void validateResult(final Transaction expected, final Transaction retrieved) {
        Assert.assertEquals("mismatched transaction id", expected.getId(), retrieved.getId());
        Assert.assertEquals("Wrong status", expected.getStatus(), retrieved.getStatus());
        Assert.assertEquals("Mismatched creation time", expected.getCreationTime(), retrieved.getCreationTime());
        Assert.assertEquals("Wrong last modified time", expected.getLastModificationTime(),
                retrieved.getLastModificationTime());
        Assert.assertEquals("Wrong saler id", expected.getSaler(), expected.getSaler());
    }
}
