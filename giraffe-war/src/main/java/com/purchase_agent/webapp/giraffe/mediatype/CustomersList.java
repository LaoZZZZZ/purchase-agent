package com.purchase_agent.webapp.giraffe.mediatype;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
public class CustomersList {
    private List<Customer> customerList = new ArrayList<>();

    public CustomersList() {
    }

    @JsonProperty("customer_list")
    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(final List<Customer> customerList) {
        this.customerList = customerList;
    }
}
