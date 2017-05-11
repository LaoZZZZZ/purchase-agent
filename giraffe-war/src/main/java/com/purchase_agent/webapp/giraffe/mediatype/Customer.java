package com.purchase_agent.webapp.giraffe.mediatype;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by lukez on 5/10/17.
 */
public class Customer {
    private long id;
    private String customerName;
    private String phoneNumber;
    // The mailing address
    private String address;
    private String wechat;

    public Customer(){
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @JsonProperty("customer_name")
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(final String customerName) {
        this.customerName = customerName;
    }

    @JsonProperty("phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    @JsonProperty("wechat")
    public String getWechat() {
        return wechat;
    }

    public void setWechat(final String wechat) {
        this.wechat = wechat;
    }
}
