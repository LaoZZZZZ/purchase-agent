package com.purchase_agent.webapp.giraffe.metrics;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
/**
 * Created by lukez on 5/4/17.
 */
public class AggregatedTransactionMetrics {
    private DateTime creationTime;
    private String username;
    private List<String> reserved;
    private List<String> paid;
    private List<String> shipped;
    private List<String> delivered;
    private List<String> returned;

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

    @JsonProperty("reserved")
    public List<String> getReserved() {
        return reserved;
    }

    public void setReserved(final List<String> reserved) {
        this.reserved = reserved;
    }

    @JsonProperty("paied")
    public List<String> getPaid() {
        return paid;
    }

    public void setPaied(final List<String> paid) {
        this.paid = paid;
    }

    @JsonProperty("shipped")
    public List<String> getShipped() {
        return shipped;
    }

    public void setShipped(final List<String> shipped) {
        this.shipped = shipped;
    }

    @JsonProperty("delivered")
    public List<String> getDelivered() {
        return delivered;
    }

    public void setDelivered(final List<String> delivered) {
        this.delivered = delivered;
    }

    @JsonProperty("returned")
    public List<String> getReturned() {
        return returned;
    }

    public void setReturned(final List<String> returned) {
        this.returned = returned;
    }
}
