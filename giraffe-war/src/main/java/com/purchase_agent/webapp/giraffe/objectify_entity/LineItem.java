package com.purchase_agent.webapp.giraffe.objectify_entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import org.joda.time.DateTime;
/**
 * Created by lukez on 2/24/17.
 */
@Entity(name = "PA_LINEITEM")
public class LineItem {
    public static enum Type {
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

    private double originalPrice;

    private double purchasePrice;

    @Index
    private Type type;

    @Index
    private String brand;

    @Index
    private DateTime purchaseTime;

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

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(final double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(final double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public DateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(final DateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(final String brand) {
        this.brand = brand;
    }
}
