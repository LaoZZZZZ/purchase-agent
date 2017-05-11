package com.purchase_agent.webapp.giraffe.mediatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
/**
 * Created by lukez on 5/9/17.
 */
public class ItemInAction {
    // line item id
    private LineItem lineItem;
    // when this item is sold
    private DateTime soldTime;
    private Customer customer;

    public ItemInAction() {
    }

    @JsonProperty("sold_time")
    public DateTime getSoldTime() {
        return soldTime;
    }

    public void setSoldTime(final DateTime soldTime) {
        this.soldTime = soldTime;
    }

    @JsonProperty("line_item")
    public LineItem getLineItem() {
        return lineItem;
    }

    public void setLineItem(final LineItem lineItem) {
        this.lineItem = lineItem;
    }

    @JsonProperty("customer")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }
}
