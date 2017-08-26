package com.purchase_agent.webapp.giraffe.objectify_entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.joda.time.DateTime;

/**
 * Created by lukez on 2/15/17.
 */
@Entity(name = "PA_CUSTOMER")
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

    @Index
    private DateTime creationTime;

    @Index
    private DateTime updateTime;

    public Customer() {
    }

    public Customer(long id) {
        this.id = id;
    }

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

    public DateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(final DateTime creationTime) {
        this.creationTime = creationTime;
    }

    public DateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(final DateTime updateTime) {
        this.updateTime = updateTime;
    }
}
