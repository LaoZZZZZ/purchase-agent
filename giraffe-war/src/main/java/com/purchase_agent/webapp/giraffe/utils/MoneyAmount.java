package com.purchase_agent.webapp.giraffe.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by lukez on 3/17/17.
 */
public class MoneyAmount {
    private String amount;
    private Currency currency;
    private static int precision = 2;

    public MoneyAmount() {
    }

    public MoneyAmount(final String amount, final Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    @JsonProperty("amount")
    public String getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        amount.setScale(precision, BigDecimal.ROUND_DOWN);
        this.amount = amount.toPlainString();
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoneyAmount that = (MoneyAmount) o;

        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        return currency == that.currency;
    }

    @Override
    public int hashCode() {
        int result = amount != null ? amount.hashCode() : 0;
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }
}
