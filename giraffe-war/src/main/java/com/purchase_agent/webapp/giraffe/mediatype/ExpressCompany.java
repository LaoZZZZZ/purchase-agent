package com.purchase_agent.webapp.giraffe.mediatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.purchase_agent.webapp.giraffe.objectify_entity.Hours;

import java.util.List;

/**
 * Created by lukez on 4/6/17.
 */
public class ExpressCompany {
    private String companyName;
    private String contact;
    private String companyAddress;
    private String trackingWebSite;
    private List<Hours> hours;
    private double rating;

    public ExpressCompany() {}

    @JsonProperty("company_name")
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @JsonProperty("contact")
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @JsonProperty("company_address")
    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    @JsonProperty("tracking_website")
    public String getTrackingWebSite() {
        return trackingWebSite;
    }

    public void setTrackingWebSite(String trackingWebSite) {
        this.trackingWebSite = trackingWebSite;
    }

    @JsonProperty("hours")
    public List<Hours> getHours() {
        return hours;
    }

    public void setHours(List<Hours> hours) {
        this.hours = hours;
    }

    @JsonProperty("rating")
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
