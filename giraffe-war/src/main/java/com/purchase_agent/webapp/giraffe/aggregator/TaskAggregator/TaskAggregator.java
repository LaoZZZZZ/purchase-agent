package com.purchase_agent.webapp.giraffe.aggregator.TaskAggregator;

import com.purchase_agent.webapp.giraffe.mediatype.ActionItem;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;

import java.util.List;
public interface TaskAggregator {
    // Return empty list if no task is created
    // This function goes through each transaction in the list. create task if necessary and return all created task in one list.
    List<ActionItem> generateTasks(final List<Transaction> transactions);
}
