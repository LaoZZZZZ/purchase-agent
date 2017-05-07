package com.purchase_agent.webapp.giraffe.filters;

/**
 * Created by lukez on 5/6/17.
 */
public class Priorities {
    public static final int LOGIN = 990;
    public static final int AUTHENTICATION = 1000;
    public static final int AUTHORIZATION = 2000;
    public static final int HEADER_DECORATOR = 3000;
    public static final int ENTITY_CODER = 4000;
    public static final int USER = 5000;
}
