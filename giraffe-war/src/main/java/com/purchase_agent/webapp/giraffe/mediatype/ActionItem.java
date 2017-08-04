package com.purchase_agent.webapp.giraffe.mediatype;

public class ActionItem {
    public enum TaskType {
        MAILING,
        PURCHASE,
        CONFIRMATION
    }

    private final TaskType taskType;
    private final LineItem lineItem;
    private final Customer customer;

    public ActionItem(final TaskType taskType, final LineItem lineItem, final Customer customer) {
        this.taskType = taskType;
        this.lineItem = lineItem;
        this.customer = customer;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public LineItem getLineItem() {
        return lineItem;
    }

    public Customer getCustomer() {
        return customer;
    }
}
