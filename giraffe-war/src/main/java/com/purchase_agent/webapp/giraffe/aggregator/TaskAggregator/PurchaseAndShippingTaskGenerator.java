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

public class PurchaseAndShippingTaskGenerator implements TaskAggregator{
    private final LineItemDao lineItemDao;
    public PurchaseAndShippingTaskGenerator(final LineItemDao lineItemDao) {
        this.lineItemDao = lineItemDao;
    }

    @Override
    public List<ActionItem> generateTasks(final List<Transaction> transactions) {
        List<ActionItem> res = new ArrayList<>();
        if (transactions == null || transactions.isEmpty()) {
            return res;
        }
        for (final Transaction transaction : transactions) {
            res.addAll(generateTasksForSingleTransaction(transaction));
        }
        return res;
    }

    private List<ActionItem> generateTasksForSingleTransaction(final Transaction transaction) {
        List<ActionItem> actionItems = new ArrayList<>();
        if (transaction != null) {
            // if the transaction is not deleted and the is paid or reserved.
            if (!transaction.isDeleted() &&
                    (transaction.getStatus() == Transaction.Status.PAID || transaction.getStatus() == Transaction.Status.RESERVE)) {
                for (final String itemId : transaction.getItemIds()) {
                    final Customer customer = Customer.buildFromPersistedEntity(
                            ofy().load().key(Key.create(com.purchase_agent.webapp.giraffe.objectify_entity.Customer.class
                                    , transaction.getCustomerId())).now());
                    LineItem item = this.lineItemDao.get().id(itemId);
                    ActionItem.TaskType type = null;
                    if (item.getStatus() == LineItem.Status.IN_STOCK) {
                        type = ActionItem.TaskType.MAILING;
                    } else if (item.getStatus() == LineItem.Status.IN_STOCK) {
                        type = ActionItem.TaskType.PURCHASE;
                    }
                    actionItems.add(new ActionItem(type, LineItemResource.toMediaType(item), customer));
                }
            }
        }
        return actionItems;
    }
}
