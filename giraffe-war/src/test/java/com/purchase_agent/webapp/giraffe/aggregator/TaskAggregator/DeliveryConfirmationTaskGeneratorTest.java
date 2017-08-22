package com.purchase_agent.webapp.giraffe.aggregator.TaskAggregator;

import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.ObjectifyService;
import com.purchase_agent.webapp.giraffe.ObjectifyBaseTest;
import com.purchase_agent.webapp.giraffe.mediatype.ActionItem;
import com.purchase_agent.webapp.giraffe.objectify_entity.Customer;
import com.purchase_agent.webapp.giraffe.objectify_entity.LineItem;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import com.purchase_agent.webapp.giraffe.persistence.LineItemDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class DeliveryConfirmationTaskGeneratorTest extends ObjectifyBaseTest{
    private LineItemDao lineItemDao;
    private DeliveryConfirmationTaskGenerator deliveryConfirmationTaskGenerator;
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
        LineItem sold = new LineItem("sold");
        sold.setStatus(LineItem.Status.SOLD);
        LINE_ITEM_MAP.put("sold", sold);
        ofy().save().entities(LINE_ITEM_MAP.values()).now();

        TRANSACTION_MAP = new HashMap<>();
        Transaction reserved = new Transaction("shipped");
        reserved.setStatus(Transaction.Status.RESERVE);
        reserved.getItemIds().addAll(LINE_ITEM_MAP.keySet());
        reserved.setCustomerId(customer.getId());
        TRANSACTION_MAP.put("shipped", reserved);

        Transaction shipped = new Transaction("shipped");
        shipped.setStatus(Transaction.Status.SHIPPED);
        shipped.getItemIds().add("sold");
        TRANSACTION_MAP.put("shipped", shipped);
        shipped.setCustomerId(customer.getId());
        ofy().save().entities(TRANSACTION_MAP.values());
        deliveryConfirmationTaskGenerator = new DeliveryConfirmationTaskGenerator(lineItemDao);
    }

    @Test
    public void test_purchase_success() {
        List<Transaction> transactionList = ImmutableList.of(TRANSACTION_MAP.get("shipped"));
        List<ActionItem> result = deliveryConfirmationTaskGenerator.generateTasks(transactionList);
        Assert.assertEquals(1, result.size());
        int numberToConfirm = 0;
        for (final ActionItem item : result) {
            if (item.getTaskType().equals(ActionItem.TaskType.CONFIRMATION)) {
                numberToConfirm += 1;
            }
        }
        Assert.assertEquals(1, numberToConfirm);
    }
}
