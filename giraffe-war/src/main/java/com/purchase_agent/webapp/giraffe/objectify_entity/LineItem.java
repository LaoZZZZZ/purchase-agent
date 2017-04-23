package com.purchase_agent.webapp.giraffe.objectify_entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import com.purchase_agent.webapp.giraffe.utils.MoneyAmount;
import org.joda.time.DateTime;
/**
 * Created by lukez on 2/24/17.
 */
@Entity(name = "PA_LINEITEM")
public class LineItem {
    public static enum Status {
        IN_STOCK,
        SOLD,
    }

    public static enum Category {
        BABY_FOOD,
        TOY,
        BAG,
        HEALTH_PRODUCT,
        CLOTHES,
        SHOES,
        OTHER
    }

    @Id
    private String id;

    @Index
    private String transactionId;

    private MoneyAmount originalPrice;

    private MoneyAmount purchasePrice;

    @Index
    private Category category;

    @Index
    private String brand;

    @Index
    private DateTime purchaseTime;

    @Index
    private Status status;

    @Index
    private String owner;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(final String transactionId) {
        this.transactionId = transactionId;
    }

    public MoneyAmount getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(final MoneyAmount originalPrice) {
        this.originalPrice = originalPrice;
    }

    public MoneyAmount getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(final MoneyAmount purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public DateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(final DateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(final String brand) {
        this.brand = brand;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }
}
