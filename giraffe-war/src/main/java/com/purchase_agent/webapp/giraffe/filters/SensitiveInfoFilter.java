package com.purchase_agent.webapp.giraffe.filters;


import java.io.IOException;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

/**
 * Created by lukez on 2/19/17.
 */
@Priority(1000)
public class SensitiveInfoFilter implements ContainerResponseFilter {
    private static final String HEADER = "X-Cloud-Trace-Context";

    @Inject
    SensitiveInfoFilter() {
    }

    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext)
            throws IOException {
        if (responseContext.getHeaders().containsKey(HEADER)) {
            responseContext.getHeaders().remove(HEADER);
        }
    }
}
