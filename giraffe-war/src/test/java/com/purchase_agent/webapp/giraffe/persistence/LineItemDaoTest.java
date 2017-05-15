package com.purchase_agent.webapp.giraffe.persistence;

import com.purchase_agent.webapp.giraffe.objectify_entity.LineItem;
import com.purchase_agent.webapp.giraffe.utils.Currency;
import com.purchase_agent.webapp.giraffe.utils.MoneyAmount;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by lukez on 4/16/17.
 */
public class LineItemDaoTest extends ObjectifyBaseTest{
    private LineItemDao lineItemDao = new LineItemDao();

    private static final DateTime PURCHASE_TIME = DateTime.parse("2016-10-15");
    private static final String TRANSACTION_ID = "transaction_id_test";
    private static final LineItem.Status STATUS = LineItem.Status.IN_STOCK;
    private static final LineItem.Category CATEGORY = LineItem.Category.BAG;
    private static final String BRAND = "coach";
    private static final MoneyAmount ORIGINAL_PRICE = new MoneyAmount(new BigDecimal("100.00"), Currency.RMB);
    private static final MoneyAmount PURCHASE_PRICE = new MoneyAmount(new BigDecimal("80.00"), Currency.RMB);


    @Test
    public void test_idLookUp_success() {
        LineItem created = createAndPersistLineItem();
        LineItem retrieved = this.lineItemDao.get().id(created.getId());
        validateResult(created, retrieved);
    }

    @Test
    public void test_get_notFound() {
        LineItem retrieved = this.lineItemDao.get().id("not_found");
        Assert.assertNull(retrieved);
    }

    @Test
    public void test_search_success() {
        LineItem created = createAndPersistLineItem();

        // search by modification time
        LineItemDao.Search.Result retrieved =
                this.lineItemDao.search().brand(created.getBrand()).execute();
        Assert.assertEquals("Expecting one result", 1, retrieved.lineItems.size());
        validateResult(created, retrieved.lineItems.get(0));


        // search by saler
        retrieved = this.lineItemDao.search().category(created.getCategory()).execute();
        Assert.assertEquals("Expecting one result", 1, retrieved.lineItems.size());
        validateResult(created, retrieved.lineItems.get(0));

        //search by customer id
        retrieved = this.lineItemDao.search().purchaseTime(created.getPurchaseTime()).execute();
        Assert.assertEquals("Expecting one result", 1, retrieved.lineItems.size());
        validateResult(created, retrieved.lineItems.get(0));

        // search by is deleted
        retrieved = this.lineItemDao.search().status(created.getStatus()).execute();
        Assert.assertEquals("Expecting one result", 1, retrieved.lineItems.size());
        validateResult(created, retrieved.lineItems.get(0));


        // search by status
        retrieved = this.lineItemDao.search().transactionId(created.getTransactionId()).execute();
        Assert.assertEquals("Expecting one result", 1, retrieved.lineItems.size());
        validateResult(created, retrieved.lineItems.get(0));
    }

    // test pagination logic
    @Test
    public void test_pagination_success() {
        // create three transaction and set the pagination size to be 2.
        Map<String, LineItem> created = new HashMap<>();
        for (int i = 0; i < 4; ++i) {
            final LineItem lineItem = createAndPersistLineItem();
            created.put(lineItem.getId(), lineItem);
        }
        LineItemDao.Search search = lineItemDao.search().brand(BRAND).limit(3);

        LineItemDao.Search.Result result = search.execute();
        // get all result.
        List<LineItem> retrievedLineItems = new ArrayList<>();
        while(true) {
            retrievedLineItems.addAll(result.lineItems);
            if (result.encodedCursor != null) {
                search = search.next(result.encodedCursor);
                result = search.execute();
            } else {
                break;
            }
        }
        Assert.assertEquals("wrong query result", created.size(), retrievedLineItems.size());
        for(final LineItem lineItem : retrievedLineItems) {
            Assert.assertTrue("missing transaction", created.containsKey(lineItem.getId()));
        }
    }

    private LineItem createAndPersistLineItem() {
        LineItem toReturn = new LineItem();
        toReturn.setId(UUID.randomUUID().toString());
        toReturn.setStatus(STATUS);
        toReturn.setPurchaseTime(PURCHASE_TIME);
        toReturn.setBrand(BRAND);
        toReturn.setCategory(CATEGORY);
        toReturn.setTransactionId(TRANSACTION_ID);
        toReturn.setPurchasePrice(PURCHASE_PRICE);
        toReturn.setOriginalPrice(ORIGINAL_PRICE);
        ofy().save().entity(toReturn).now();
        return toReturn;
    }

    private void validateResult(final LineItem expected, final LineItem retrieved) {
        Assert.assertEquals("mismatched line item id", expected.getId(), retrieved.getId());
        Assert.assertEquals("Wrong status", expected.getStatus(), retrieved.getStatus());
        Assert.assertEquals("Mismatched purchase time", expected.getPurchaseTime(), retrieved.getPurchaseTime());
        Assert.assertEquals("Wrong brand", expected.getBrand(), retrieved.getBrand());
        Assert.assertEquals("Wrong category", expected.getCategory(), retrieved.getCategory());
        Assert.assertEquals("Wrong original price", expected.getOriginalPrice(), retrieved.getOriginalPrice());
        Assert.assertEquals("Wrong purchase price", expected.getPurchasePrice(), retrieved.getPurchasePrice());
    }
}
