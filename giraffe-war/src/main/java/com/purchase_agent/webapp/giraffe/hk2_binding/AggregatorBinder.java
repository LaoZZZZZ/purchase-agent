package com.purchase_agent.webapp.giraffe.hk2_binding;

import com.purchase_agent.webapp.giraffe.aggregator.MetricsAggregator.TransactionsAggregator;
import com.purchase_agent.webapp.giraffe.aggregator.MetricsAggregator.TransactionsContainer;
import com.purchase_agent.webapp.giraffe.aggregator.TaskAggregator.PurchaseAndShippingTaskGenerator;
import com.purchase_agent.webapp.giraffe.aggregator.TaskAggregator.DeliveryConfirmationTaskGenerator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class AggregatorBinder extends AbstractBinder {
    @Override
    public void configure() {
        bindAsContract(TransactionsContainer.class);
        bindAsContract(TransactionsAggregator.class);
        bindAsContract(PurchaseAndShippingTaskGenerator.class);
        bindAsContract(DeliveryConfirmationTaskGenerator.class);
    }
}
