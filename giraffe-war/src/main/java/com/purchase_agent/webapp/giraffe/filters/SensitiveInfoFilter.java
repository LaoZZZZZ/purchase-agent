package com.purchase_agent.webapp.giraffe.filters;

import java.io.IOException;
import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * Created by lukez on 2/19/17.
 */
@Priority(Priorities.HEADER_DECORATOR)
@Provider
public class SensitiveInfoFilter implements ContainerResponseFilter {
    private static final Logger logger = Logger.getLogger(SensitiveInfoFilter.class.getName());
    private static final String HEADER = "X-Cloud-Trace-Context";

    @Inject
    public SensitiveInfoFilter() {
    }

    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext)
            throws IOException {
        if (responseContext.getHeaders().containsKey(HEADER)) {
            logger.info("remove the response header: " + responseContext.getHeaders().get(HEADER));
            responseContext.getHeaders().remove(HEADER);
        }
    }

    public String getHeader() {
        return this.HEADER;
    }
}
