package com.purchase_agent.webapp.giraffe.utils;

import com.google.common.collect.ImmutableMap;
import com.purchase_agent.webapp.giraffe.resource.UserResource;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.UriBuilder;
import java.util.Map;
import java.net.URI;
/**
 * Created by lukez on 2/20/17.
 */
@Singleton
public class Links {
    private final static Map<Environment, String> HOSTNAME = ImmutableMap.of(
            Environment.DEVELOPMENT, "purchase-agent-dev.appspot.com",
            Environment.PRODUCTION, "purchase-agent.appspot.com",
            Environment.TEST, "purchase-agent-dev.appspot.com"
            );
    private String hostname;
    @Inject
    public Links(Environment environment) {
        this.hostname = HOSTNAME.get(environment);
    }

    public static URI forUserCreation(final String activationToken) {
        return UriBuilder.fromResource(UserResource.class)
                .path("/activate/{activation_token}")
                .resolveTemplate("activation_token", activationToken)
                .build();
    }

}
