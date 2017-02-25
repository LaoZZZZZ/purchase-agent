package com.purchase_agent.webapp.giraffe.objectify_entity;

import  com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by lukez on 2/15/17.
 */
@Entity
public class Customer {
    @Id
    private long id;

    @Index
    private String customerName;

    @Index
    private String phoneNumber;

    // The mailing address
    private String address;

    // The wechat id
    @Index
    private String wechat;

    public long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(final String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(final String wechat) {
        this.wechat = wechat;
    }
}
