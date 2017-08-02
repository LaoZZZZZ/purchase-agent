package com.purchase_agent.webapp.giraffe.aggregator.TaskAggregator;

import com.purchase_agent.webapp.giraffe.mediatype.ActionItem;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;

import java.util.ArrayList;
import java.util.List;

public class PurchaseTaskGenerator implements TaskAggregator{
    @Override
    public List<ActionItem> generateTasks(final List<Transaction> transactions) {
        return new ArrayList<>();
    }
}
