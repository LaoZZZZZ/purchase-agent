package com.purchase_agent.webapp.giraffe.filters;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * Created by lukez on 2/27/17.
 */
@Priority(Priorities.HEADER_DECORATOR)
@Provider
public class UserAuthResponseFilter implements ContainerResponseFilter {
    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) {
        responseContext.getHeaders().add("authorization", requestContext.getHeaders().get("authorization"));
    }
}
