package com.purchase_agent.webapp.giraffe.mediatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.purchase_agent.webapp.giraffe.utils.MoneyAmount;
import org.joda.time.DateTime;
import java.util.List;
/**
 * Media type of transaction entity.
 * Created by lukez on 3/11/17.
 */
public class Transaction {
    private String transactionId;
    private com.purchase_agent.webapp.giraffe.objectify_entity.Transaction.Status status;
    private MoneyAmount moneyAmount;
    private List<String> lineItemIds;
    private DateTime creationTime;
    private DateTime lastModificationTime;

    public Transaction() {

    }

    @JsonProperty("transaction_id")
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(final String transactionId) {
        this.transactionId = transactionId;
    }

    @JsonProperty("status")
    public com.purchase_agent.webapp.giraffe.objectify_entity.Transaction.Status getStatus() {
        return status;
    }

    public void setStatus(com.purchase_agent.webapp.giraffe.objectify_entity.Transaction.Status status) {
        this.status = status;
    }

    @JsonProperty("money_amount")
    public MoneyAmount getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(MoneyAmount moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    @JsonProperty("line_item_ids")
    public List<String> getLineItemIds() {
        return lineItemIds;
    }

    public void setLineItemIds(List<String> lineItemIds) {
        this.lineItemIds = lineItemIds;
    }

    @JsonProperty("creation_time")
    public DateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(final DateTime creationTime) {
        this.creationTime = creationTime;
    }

    public DateTime getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(final DateTime lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }
}

