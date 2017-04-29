package com.purchase_agent.webapp.giraffe.authentication;

import com.google.appengine.repackaged.com.google.common.base.Preconditions;

import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by lukez on 4/24/17.
 */
public class SecurityContextWrapper {
    private final SecurityContext securityContext;
    private UserAuthModel userAuthModel;

    @Inject
    public SecurityContextWrapper(final SecurityContext securityContext) {
        this.securityContext = securityContext;
        UserPrincipal userPrincipal = (UserPrincipal) this.securityContext.getUserPrincipal();
        UserAuthModel authModel = userPrincipal.getUser();
        Preconditions.checkNotNull(authModel);
    }

    public UserAuthModel getUserInfo() {
        return userAuthModel;
    }

    public boolean isUserInRole(final String role) {
        return this.securityContext.isUserInRole(role);
    }
}
