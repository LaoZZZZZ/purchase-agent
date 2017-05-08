package com.purchase_agent.webapp.giraffe.mediatype;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.util.List;
/**
 * Created by lukez on 5/7/17.
 */
public class WhiteListedUser {
    private String username;
    private String userEmail;
    private List<String> roles;
    private DateTime expirationTime;
    private boolean isDeleted;

    public WhiteListedUser() {
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @JsonProperty("user_email")
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(final String userEmail) {
        this.userEmail = userEmail;
    }

    @JsonProperty("roles")
    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(final List<String> roles) {
        this.roles = roles;
    }

    @JsonProperty("expiration_time")
    public DateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(final DateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    @JsonProperty("is_deleted")
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
