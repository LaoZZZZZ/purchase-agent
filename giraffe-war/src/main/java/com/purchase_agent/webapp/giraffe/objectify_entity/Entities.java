package com.purchase_agent.webapp.giraffe.objectify_entity;

import com.google.common.collect.ImmutableList;
import java.util.List;
/**
 * Created by lukez on 2/19/17.
 */
public class Entities {
    public static final List<? extends Object> entities = ImmutableList.of(
            Customer.class,
            ExpressCompany.class,
            Hours.class,
            User.class);

}
