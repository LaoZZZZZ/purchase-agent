package com.purchase_agent.webapp.giraffe.objectify_entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.joda.time.DateTime;

/**
 * Created by lukez on 2/24/17.
 */
@Entity(name = "PA_TRANSACTION")
public class Transaction {
    public static enum Status {
        RESERVE,
        PAID,
        SHIPPED,
        DELIVERED,
        RETURNED
    }
    @Id
    private String id;

    @Index
    private long customerId;

    @Index
    private DateTime lastModificationTime;

    @Index
    private double moneyAmount;

    @Index
    private String saler;

    @Index
    private String itemId;

    private int numOfItems;

    @Index
    private Status status;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final long customerId) {
        this.customerId = customerId;
    }

    public DateTime getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(final DateTime lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }

    public double getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(final double moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public String getSaler() {
        return saler;
    }

    public void setSaler(final String saler) {
        this.saler = saler;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(final String itemId) {
        this.itemId = itemId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public int getNumOfItems() {
        return numOfItems;
    }

    public void setNumOfItems(final int numOfItems) {
        this.numOfItems = numOfItems;
    }
}
