package com.purchase_agent.webapp.giraffe.objectify_entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.List;
import org.joda.time.DateTime;
/**
 * Created by lukez on 3/12/17.
 */
@Entity(name = "PA_WhiteListedUser")
public class WhiteListedUser {
    @Id
    private String userName;

    @Index
    private String userEmail;

    @Index
    private List<String> roles;

    @Index
    private boolean isDeleted;

    @Index
    private DateTime createdTime;

    @Index
    private DateTime expirationTime;

    WhiteListedUser() {
    }

    public WhiteListedUser(final String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(final String userEmail) {
        this.userEmail = userEmail;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(final List<String> roles) {
        this.roles = roles;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(final boolean deleted) {
        isDeleted = deleted;
    }

    public DateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(final DateTime createdTime) {
        this.createdTime = createdTime;
    }

    public DateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(final DateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
}
