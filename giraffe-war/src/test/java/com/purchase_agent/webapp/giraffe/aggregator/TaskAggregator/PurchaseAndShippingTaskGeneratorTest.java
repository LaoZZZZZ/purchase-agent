package com.purchase_agent.webapp.giraffe.aggregator.TaskAggregator;

import com.google.common.collect.ImmutableList;
import com.purchase_agent.webapp.giraffe.mediatype.ActionItem;
import com.purchase_agent.webapp.giraffe.objectify_entity.Customer;
import com.purchase_agent.webapp.giraffe.objectify_entity.LineItem;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import com.purchase_agent.webapp.giraffe.persistence.LineItemDao;
import com.purchase_agent.webapp.giraffe.ObjectifyBaseTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class PurchaseAndShippingTaskGeneratorTest extends ObjectifyBaseTest {
    private PurchaseAndShippingTaskGenerator purchaseAndShippingTaskGenerator;
    private LineItemDao lineItemDao;

    private Map<String, Transaction> TRANSACTION_MAP;
    private Map<String, LineItem> LINE_ITEM_MAP;

    @Before
    public void setUpTest() {
        super.setUp();
        lineItemDao = new LineItemDao();
        Customer customer = new Customer(1);
        ofy().save().entity(customer).now();
        customer = ofy().load().entity(customer).now();

        LINE_ITEM_MAP = new HashMap<>();
        LineItem inStock = new LineItem("in_stock");
        inStock.setStatus(LineItem.Status.IN_STOCK);
        LINE_ITEM_MAP.put("in_stock", inStock);
        LineItem inDemand = new LineItem("in_demand");
        inDemand.setStatus(LineItem.Status.IN_DEMAND);
        LINE_ITEM_MAP.put("in_demand", inDemand);
        LineItem inDemand2 = new LineItem("in_demand_2");
        inDemand2.setStatus(LineItem.Status.IN_DEMAND);
        LINE_ITEM_MAP.put("in_demand_2", inDemand2);
        ofy().save().entities(LINE_ITEM_MAP.values()).now();

        TRANSACTION_MAP = new HashMap<>();
        Transaction reserved = new Transaction("reserved");
        reserved.setStatus(Transaction.Status.RESERVE);
        reserved.getItemIds().addAll(LINE_ITEM_MAP.keySet());
        reserved.setCustomerId(customer.getId());
        TRANSACTION_MAP.put("reserved", reserved);

        Transaction paid = new Transaction("paid");
        paid.setStatus(Transaction.Status.PAID);
        paid.getItemIds().addAll(LINE_ITEM_MAP.keySet());
        TRANSACTION_MAP.put("paid", paid);
        paid.setCustomerId(customer.getId());
        ofy().save().entities(TRANSACTION_MAP.values());
        purchaseAndShippingTaskGenerator = new PurchaseAndShippingTaskGenerator(lineItemDao);
    }

    @Test
    public void test_purchase_success() {
        List<Transaction> transactionList = ImmutableList.of(TRANSACTION_MAP.get("reserved"));
        List<ActionItem> result = purchaseAndShippingTaskGenerator.generateTasks(transactionList);
        Assert.assertEquals(3, result.size());
        int numberToPurchaseItems = 0;
        int numberToMail = 0;
        for (final ActionItem item : result) {
            if (item.getTaskType().equals(ActionItem.TaskType.PURCHASE)) {
                numberToPurchaseItems += 1;
            } else if (item.getTaskType().equals(ActionItem.TaskType.MAILING)) {
                numberToMail += 1;
            }
        }
        Assert.assertEquals(2, numberToPurchaseItems);
        Assert.assertEquals(1, numberToMail);
    }
}
