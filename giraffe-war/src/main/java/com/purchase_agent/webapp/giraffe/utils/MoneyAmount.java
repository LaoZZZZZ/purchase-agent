package com.purchase_agent.webapp.giraffe.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;

import java.math.BigDecimal;

/**
 * Money amount.
 * it is an immutable class.
 * Created by lukez on 3/17/17.
 */
public class MoneyAmount {
    public static final MoneyAmount ZERO = new MoneyAmount();
    private String amount;
    private Currency currency;
    private static int precision = 2;

    public MoneyAmount() {
        this(BigDecimal.ZERO.setScale(precision, BigDecimal.ROUND_DOWN), Currency.USD);
    }

    public MoneyAmount(final BigDecimal amount, final Currency currency) {
        this.setAmount(amount);
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

    public static MoneyAmount add(final MoneyAmount op1, final MoneyAmount op2) {
        Preconditions.checkState(op1.getCurrency() == op2.getCurrency(), "mismatched currency");
        MoneyAmount toReturn = new MoneyAmount();
        toReturn.setCurrency(op1.getCurrency());
        toReturn.setAmount(new BigDecimal(op1.getAmount()).add(new BigDecimal(op2.getAmount())));
        return toReturn;
    }
}
