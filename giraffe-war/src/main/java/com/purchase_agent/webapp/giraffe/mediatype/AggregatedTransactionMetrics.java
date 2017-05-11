package com.purchase_agent.webapp.giraffe.mediatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import org.joda.time.DateTime;

import java.util.Objects;

/**
 * Created by lukez on 5/4/17.
 */
public class AggregatedTransactionMetrics {
    private DateTime creationTime;
    private String username;
    private double dailyEarningInCents;
    private double weekLyEarningInCents;
    private double monthlyEarningInCents;
    private double annualEarningInCents;

    public AggregatedTransactionMetrics() {
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
    public double getDailyEarningInCents() {
        return dailyEarningInCents;
    }

    public void setDailyEarningInCents(final double dailyEarningInCents) {
        this.dailyEarningInCents = dailyEarningInCents;
    }

    @JsonProperty("weekly_earning_in_cents")
    public double getWeekLyEarningInCents() {
        return weekLyEarningInCents;
    }

    public void setWeekLyEarningInCents(final double weekLyEarningInCents) {
        this.weekLyEarningInCents = weekLyEarningInCents;
    }

    @JsonProperty("monthly_earning_in_cents")
    public double getMonthlyEarningInCents() {
        return monthlyEarningInCents;
    }

    public void setMonthlyEarningInCents(final double monthlyEarningInCents) {
        this.monthlyEarningInCents = monthlyEarningInCents;
    }

    @JsonProperty("annual_earning_in_cents")
    public double getAnnualEarningInCents() {
        return annualEarningInCents;
    }

    public void setAnnualEarningInCents(final double annualEarningInCents) {
        this.annualEarningInCents = annualEarningInCents;
    }

    public static AggregatedTransactionMetrics sum(final AggregatedTransactionMetrics metrics1,
                                                   final AggregatedTransactionMetrics metrics2) {
        Preconditions.checkArgument(Objects.equals(metrics1.getUsername(), metrics2.getUsername()));
        AggregatedTransactionMetrics toReturn = new AggregatedTransactionMetrics();
        toReturn.setUsername(metrics1.getUsername());
        toReturn.setCreationTime(metrics1.getCreationTime().isBefore(metrics2.getCreationTime()) ?
                metrics1.getCreationTime():metrics2.getCreationTime());
        toReturn.setAnnualEarningInCents(metrics1.getAnnualEarningInCents() + metrics2.getAnnualEarningInCents());
        toReturn.setMonthlyEarningInCents(metrics1.getMonthlyEarningInCents() + metrics2.getMonthlyEarningInCents());
        toReturn.setWeekLyEarningInCents(metrics1.getWeekLyEarningInCents() + metrics2.getWeekLyEarningInCents());
        toReturn.setDailyEarningInCents(metrics1.getDailyEarningInCents() + metrics2.getDailyEarningInCents());
        return toReturn;
    }
}
