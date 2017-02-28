package com.purchase_agent.webapp.giraffe.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
/**
 * Created by lukez on 2/27/17.
 */
public class UserAuthModel {
    private String username;
    private String password;
    private DateTime expireTime;
    private String authTicket;

    public UserAuthModel() {

    }

    public UserAuthModel(String username, String password, DateTime expireTime, String authTicket) {
        this.username = username;
        this.password = password;
        this.expireTime = expireTime;
        this.authTicket = authTicket;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @JsonProperty("expire_time")
    public DateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(final DateTime expireTime) {
        this.expireTime = expireTime;
    }

    @JsonProperty("auth_ticket")
    public String getAuthTicket() {
        return authTicket;
    }

    public void setAuthTicket(final String authTicket) {
        this.authTicket = authTicket;
    }
}
