package com.purchase_agent.webapp.giraffe.aggregator.TaskAggregator;

import com.googlecode.objectify.Key;
import com.purchase_agent.webapp.giraffe.mediatype.ActionItem;
import com.purchase_agent.webapp.giraffe.mediatype.Customer;
import com.purchase_agent.webapp.giraffe.objectify_entity.LineItem;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import com.purchase_agent.webapp.giraffe.persistence.LineItemDao;
import com.purchase_agent.webapp.giraffe.resource.LineItemResource;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class DeliveryConfirmationTaskGenerator implements TaskAggregator {
    private static final Logger logger = Logger.getLogger(DeliveryConfirmationTaskGenerator.class.getName());
    private LineItemDao lineItemDao;

    @Inject
    public DeliveryConfirmationTaskGenerator(final LineItemDao lineItemDao) {
        this.lineItemDao = lineItemDao;
    }

    @Override
    public List<ActionItem> generateTasks(final List<Transaction> transactions) {
        List<ActionItem> returns = new ArrayList<>();
        if (transactions != null) {
            for (final Transaction transaction : transactions) {
                if (!transaction.isDeleted() && transaction.getStatus() == Transaction.Status.SHIPPED) {
                    if (transaction.getNumOfItems() == 0) {
                        logger.warning("The shipped transaction does not have any item!");
                    }
                    for (final String itemId : transaction.getItemIds()) {
                        final Customer customer = Customer.buildFromPersistedEntity(
                                ofy().load().key(Key.create(com.purchase_agent.webapp.giraffe.objectify_entity.Customer.class
                                        , transaction.getCustomerId())).now());
                        LineItem item = this.lineItemDao.get().id(itemId);
                        if (item.getStatus() != LineItem.Status.SOLD) {
                            logger.warning("Invalid line item status: " + item.getStatus());
                        }
                        ActionItem.TaskType type = ActionItem.TaskType.CONFIRMATION;
                        returns.add(new ActionItem(type, LineItemResource.toMediaType(item), customer));
                    }
                }
            }
        }
        return returns;
    }
}
