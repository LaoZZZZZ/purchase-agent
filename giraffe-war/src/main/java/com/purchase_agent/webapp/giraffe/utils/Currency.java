package com.purchase_agent.webapp.giraffe.utils;

/**
 * Created by lukez on 3/17/17.
 */
public enum Currency {
    RMB("RMB"),
    USD("US");

    private String value;
    Currency(final String value) {
        this.value = value;
    }
}
