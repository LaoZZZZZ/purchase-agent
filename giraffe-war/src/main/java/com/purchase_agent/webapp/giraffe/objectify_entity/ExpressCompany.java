package com.purchase_agent.webapp.giraffe.objectify_entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.List;
/**
 * Created by lukez on 2/15/17.
 */
@Entity(name = "PA_EXPRESS_COMPANY")
public class ExpressCompany {
    public enum Status {
        CLOSED,
        OPEN,
    }

    @Id
    private String id;

    @Index
    private String companyName;

    @Index
    private String contact;

    @Index
    private String companyAddress;

    private String trackingWebSite;

    private double rating;

    private List<Hours> hours;

    @Index
    private DateTime creationTime;

    public ExpressCompany() {
    }
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(final String companyName) {
        this.companyName = companyName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(final String contact) {
        this.contact = contact;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(final String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getTrackingWebSite() {
        return trackingWebSite;
    }

    public void setTrackingWebSite(final String trackingWebSite) {
        this.trackingWebSite = trackingWebSite;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(final double rating) {
        this.rating = rating;
    }

    public List<Hours> getHours() {
        return hours;
    }

    public void setHours(final List<Hours> hours) {
        this.hours = hours;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(final DateTime creationTime) {
        this.creationTime = creationTime;
    }
}
