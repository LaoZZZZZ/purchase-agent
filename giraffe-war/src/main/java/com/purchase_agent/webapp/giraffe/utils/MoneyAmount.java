package com.purchase_agent.webapp.giraffe.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by lukez on 3/17/17.
 */
public class MoneyAmount {
    private BigDecimal amount;
    private Currency currency;
    private static int precision = 2;

    public MoneyAmount() {

    }

    @JsonProperty("amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        amount.setScale(precision, BigDecimal.ROUND_DOWN);
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}