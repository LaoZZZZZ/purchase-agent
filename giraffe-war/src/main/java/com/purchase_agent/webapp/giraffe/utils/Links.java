package com.purchase_agent.webapp.giraffe.utils;

import com.google.common.collect.ImmutableMap;
import com.purchase_agent.webapp.giraffe.resource.UserResource;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Context;
import java.util.Map;
import java.net.URI;
/**
 * Created by lukez on 2/20/17.
 */
@Singleton
public class Links {
    private UriInfo uriInfo;
    @Inject
    public Links(@Context UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public  URI forUserCreation(final String activationToken) {
        return this.uriInfo.getBaseUriBuilder().fromResource(UserResource.class)
                .path("/activate/{activation_token}")
                .resolveTemplate("activation_token", activationToken)
                .build();
    }
}
