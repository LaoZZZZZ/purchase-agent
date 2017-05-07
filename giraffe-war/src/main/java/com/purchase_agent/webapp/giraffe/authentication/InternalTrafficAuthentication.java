package com.purchase_agent.webapp.giraffe.authentication;

import com.google.appengine.repackaged.com.google.common.collect.ImmutableSet;
import com.google.common.base.Strings;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.logging.Logger;

/**
 * Authenticate internal traffic
 * Created by lukez on 5/6/17.
 */
public class InternalTrafficAuthentication {
    private static final Logger logger = Logger.getLogger(InternalTrafficAuthentication.class.getName());
    private static final String CRON_JOB_HEADER = "X-Appengine-Cron";

    @Inject
    public InternalTrafficAuthentication() {
    }

    public SecurityContext authenticate(final ContainerRequestContext containerRequestContext) {
        final String cronJob = containerRequestContext.getHeaderString(CRON_JOB_HEADER);
        if (!Strings.isNullOrEmpty(cronJob) || Boolean.parseBoolean(cronJob)) {
            UserAuthModel userAuthModel = new UserAuthModel();

            Principal principal = new UserPrincipal(userAuthModel);
            logger.info("This request is a cron job");
            return PASecurityContext.createSecurityContext(
                    ImmutableSet.of(Roles.CRON), principal, PASecurityContext.Schema.INTERNAL);
        }
        return null;
    }
}
