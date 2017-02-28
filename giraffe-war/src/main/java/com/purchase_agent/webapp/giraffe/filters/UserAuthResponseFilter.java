package com.purchase_agent.webapp.giraffe.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

/**
 * Created by lukez on 2/27/17.
 */
public class UserAuthResponseFilter implements ContainerResponseFilter {
    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) {
        responseContext.getHeaders().add("authorization", requestContext.getHeaders().get("authorization"));
    }
}
