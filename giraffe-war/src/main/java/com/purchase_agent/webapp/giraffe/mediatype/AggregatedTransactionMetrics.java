package com.purchase_agent.webapp.giraffe.mediatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import org.joda.time.DateTime;
import com.purchase_agent.webapp.giraffe.utils.MoneyAmount;
import java.util.Objects;

/**
 * Aggregated metrics for single user.
 * It is an immutable class.
 * Created by lukez on 5/4/17.
 */
public class AggregatedTransactionMetrics {
    private DateTime creationTime;
    private String username;
    private MoneyAmount dailyEarningInCents;
    private MoneyAmount weekLyEarningInCents;
    private MoneyAmount monthlyEarningInCents;
    private MoneyAmount annualEarningInCents;

    public AggregatedTransactionMetrics() {
    }

    // Initialize with zero amounts.
    public
    AggregatedTransactionMetrics(final String username, final DateTime creationTime) {
        this.username = username;
        this.creationTime = creationTime;
        this.dailyEarningInCents = MoneyAmount.ZERO;
        this.weekLyEarningInCents = MoneyAmount.ZERO;
        this.monthlyEarningInCents = MoneyAmount.ZERO;
        this.annualEarningInCents = MoneyAmount.ZERO;
    }

    @JsonProperty("creation_time")
    public DateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(final DateTime creationTime) {
        this.creationTime = creationTime;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @JsonProperty("daily_earning_in_cents")
    public MoneyAmount getDailyEarningInCents() {
        return dailyEarningInCents;
    }

    public void setDailyEarningInCents(final MoneyAmount dailyEarningInCents) {
        this.dailyEarningInCents = dailyEarningInCents;
    }

    @JsonProperty("weekly_earning_in_cents")
    public MoneyAmount getWeekLyEarningInCents() {
        return weekLyEarningInCents;
    }

    public void setWeekLyEarningInCents(final MoneyAmount weekLyEarningInCents) {
        this.weekLyEarningInCents = weekLyEarningInCents;
    }

    @JsonProperty("monthly_earning_in_cents")
    public MoneyAmount getMonthlyEarningInCents() {
        return monthlyEarningInCents;
    }

    public void setMonthlyEarningInCents(final MoneyAmount monthlyEarningInCents) {
        this.monthlyEarningInCents = monthlyEarningInCents;
    }

    @JsonProperty("annual_earning_in_cents")
    public MoneyAmount getAnnualEarningInCents() {
        return annualEarningInCents;
    }

    public void setAnnualEarningInCents(final MoneyAmount annualEarningInCents) {
        this.annualEarningInCents = annualEarningInCents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AggregatedTransactionMetrics that = (AggregatedTransactionMetrics) o;

        if (creationTime != null ? !creationTime.equals(that.creationTime) : that.creationTime != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (dailyEarningInCents != null ? !dailyEarningInCents.equals(that.dailyEarningInCents) : that.dailyEarningInCents != null)
            return false;
        if (weekLyEarningInCents != null ? !weekLyEarningInCents.equals(that.weekLyEarningInCents) : that.weekLyEarningInCents != null)
            return false;
        if (monthlyEarningInCents != null ? !monthlyEarningInCents.equals(that.monthlyEarningInCents) : that.monthlyEarningInCents != null)
            return false;
        return annualEarningInCents != null ? annualEarningInCents.equals(that.annualEarningInCents) : that.annualEarningInCents == null;
    }

    @Override
    public int hashCode() {
        int result = creationTime != null ? creationTime.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (dailyEarningInCents != null ? dailyEarningInCents.hashCode() : 0);
        result = 31 * result + (weekLyEarningInCents != null ? weekLyEarningInCents.hashCode() : 0);
        result = 31 * result + (monthlyEarningInCents != null ? monthlyEarningInCents.hashCode() : 0);
        result = 31 * result + (annualEarningInCents != null ? annualEarningInCents.hashCode() : 0);
        return result;
    }

    public static AggregatedTransactionMetrics sum(final AggregatedTransactionMetrics metrics1,
                                                   final AggregatedTransactionMetrics metrics2) {
        Preconditions.checkArgument(Objects.equals(metrics1.getUsername(), metrics2.getUsername()));
        AggregatedTransactionMetrics toReturn = new AggregatedTransactionMetrics();
        toReturn.setUsername(metrics1.getUsername());
        toReturn.setCreationTime(metrics1.getCreationTime().isBefore(metrics2.getCreationTime()) ?
                metrics1.getCreationTime():metrics2.getCreationTime());
        toReturn.setAnnualEarningInCents(MoneyAmount.add(metrics1.getAnnualEarningInCents(), metrics2.getAnnualEarningInCents()));
        toReturn.setMonthlyEarningInCents(MoneyAmount.add(metrics1.getMonthlyEarningInCents(), metrics2.getMonthlyEarningInCents()));
        toReturn.setWeekLyEarningInCents(MoneyAmount.add(metrics1.getWeekLyEarningInCents(), metrics2.getWeekLyEarningInCents()));
        toReturn.setDailyEarningInCents(MoneyAmount.add(metrics1.getDailyEarningInCents(), metrics2.getDailyEarningInCents()));
        return toReturn;
    }
}
