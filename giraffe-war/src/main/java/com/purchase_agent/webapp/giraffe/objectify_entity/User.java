package com.purchase_agent.webapp.giraffe.objectify_entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import com.googlecode.objectify.condition.IfNotNull;
import org.joda.time.DateTime;
import java.util.List;

/**
 * Created by lukez on 2/18/17.
 */
@Entity
public class User {
    public enum Status {
        ACTIVE,
        SUSPENDED,
        CLOSED,
        UNDER_VERIFICATION
    }

    @Id
    private long userId;

    @Index
    private String username;

    private String password;

    private List<String> recoverCodes;

    @Index
    private DateTime creationTime;

    @Index
    private String email;

    private DateTime lastModifiedTime;

    private String address;

    private Status status;

    @Index
    private String phoneNumber;

    @Index(IfNotNull.class)
    private String activationToken;

    public long getUserId() {
        return userId;
    }

    public void setUserId(final long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public List<String> getRecoverCode() {
        return recoverCodes;
    }

    public void setRecoverCode(final List<String> recoverCodes) {
        this.recoverCodes = recoverCodes;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(final DateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public DateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(final DateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public List<String> getRecoverCodes() {
        return recoverCodes;
    }

    public void setRecoverCodes(final List<String> recoverCodes) {
        this.recoverCodes = recoverCodes;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }
}
