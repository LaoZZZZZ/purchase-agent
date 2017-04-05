package com.purchase_agent.webapp.giraffe.mediatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.purchase_agent.webapp.giraffe.utils.MoneyAmount;
import org.joda.time.DateTime;

/**
 * Created by lukez on 4/5/17.
 */
public class LineItem {
    private com.purchase_agent.webapp.giraffe.objectify_entity.LineItem.Status status;
    private com.purchase_agent.webapp.giraffe.objectify_entity.LineItem.Category category;
    private MoneyAmount originalPrice;
    private MoneyAmount purchasePrice;
    private DateTime purchaseTime;
    private String brand;
    private String id;

    public LineItem() {
    }

    @JsonProperty("status")
    public com.purchase_agent.webapp.giraffe.objectify_entity.LineItem.Status getStatus() {
        return status;
    }

    public void setStatus(com.purchase_agent.webapp.giraffe.objectify_entity.LineItem.Status status) {
        this.status = status;
    }

    @JsonProperty("category")
    public com.purchase_agent.webapp.giraffe.objectify_entity.LineItem.Category getCategory() {
        return category;
    }

    public void setCategory(com.purchase_agent.webapp.giraffe.objectify_entity.LineItem.Category category) {
        this.category = category;
    }

    @JsonProperty("original_price")
    public MoneyAmount getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(MoneyAmount originalPrice) {
        this.originalPrice = originalPrice;
    }

    @JsonProperty("purchase_price")
    public MoneyAmount getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(MoneyAmount purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    @JsonProperty("purchase_time")
    public DateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(DateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    @JsonProperty("brand")
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
