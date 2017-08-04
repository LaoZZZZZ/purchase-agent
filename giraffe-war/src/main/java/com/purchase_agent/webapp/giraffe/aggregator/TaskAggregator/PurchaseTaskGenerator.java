package com.purchase_agent.webapp.giraffe.aggregator.TaskAggregator;

import com.googlecode.objectify.Key;
import com.purchase_agent.webapp.giraffe.mediatype.ActionItem;
import com.purchase_agent.webapp.giraffe.mediatype.Customer;
import com.purchase_agent.webapp.giraffe.objectify_entity.LineItem;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import com.purchase_agent.webapp.giraffe.persistence.LineItemDao;
import com.purchase_agent.webapp.giraffe.resource.LineItemResource;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class PurchaseTaskGenerator implements TaskAggregator{
    private final LineItemDao lineItemDao;

    public PurchaseTaskGenerator(final LineItemDao lineItemDao) {
        this.lineItemDao = lineItemDao;
    }
    @Override
    public List<ActionItem> generateTasks(final List<Transaction> transactions) {
        List<ActionItem> actionItems = new ArrayList<>();
        if (transactions != null) {
            for (final Transaction transaction : transactions) {
                if (!transaction.isDeleted() &&
                        (transaction.getStatus() == Transaction.Status.PAID || transaction.getStatus() == Transaction.Status.RESERVE)) {
                    for (final String itemId : transaction.getItemIds()) {
                        final LineItem item = lineItemDao.get().id(itemId);
                        if (item.getStatus() == LineItem.Status.IN_DEMAND) {
                            final Customer customer = Customer.buildFromPersistedEntity(
                                    ofy().load().key(Key.create(com.purchase_agent.webapp.giraffe.objectify_entity.Customer.class
                                            , transaction.getCustomerId())).now());
                            actionItems.add(new ActionItem(ActionItem.TaskType.PURCHASE, LineItemResource.toMediaType(item), customer));
                        }
                    }
                }
            }
        }
        return new ArrayList<>();
    }
}
