package com.purchase_agent.webapp.giraffe.objectify_entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by lukez on 2/15/17.
 */
@Entity
public class ExpressCompany {
    public enum Status {
        CLOSED,
        OPEN,
    }

    @Id
    private long id;

    @Index
    private String companyName;

    @Index
    private String contact;

    @Index
    private String companyAddress;

    private String trackingWebSite;

    private double rating;

    //TODO(lukez): Come up a data model to represent hours.
    private String hours;
}
