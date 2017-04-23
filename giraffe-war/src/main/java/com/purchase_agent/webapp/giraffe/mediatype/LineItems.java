package com.purchase_agent.webapp.giraffe.mediatype;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by lukez on 4/22/17.
 */
public class LineItems {
    private List<LineItem> lineItems;

    public LineItems() {
    }

    @JsonProperty("line_items")
    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(final List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }
}
