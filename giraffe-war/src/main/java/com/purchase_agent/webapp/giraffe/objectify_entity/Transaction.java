package com.purchase_agent.webapp.giraffe.objectify_entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.joda.time.DateTime;
import java.util.List;

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
    private List<String> itemIds;

    private int numOfItems;

    @Index
    private Status status;

    @Index
    private boolean isDeleted;

    @Index
    private DateTime creationTime;

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

    public List<String> getItemIds() {
        return itemIds;
    }

    public void setItemIds(final List<String> itemIds) {
        this.itemIds = itemIds;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(final boolean deleted) {
        isDeleted = deleted;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(final DateTime creationTime) {
        this.creationTime = creationTime;
    }
}
