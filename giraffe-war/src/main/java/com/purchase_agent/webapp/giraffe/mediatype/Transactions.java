package com.purchase_agent.webapp.giraffe.mediatype;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by lukez on 4/17/17.
 */
public class Transactions {
    List<Transaction> transactions;

    public Transactions() {
    }

    @JsonProperty("transactions")
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}

