package com.purchase_agent.webapp.giraffe.authentication;

import com.google.common.base.Preconditions;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by lukez on 4/24/17.
 */
public class SecurityContextWrapper {
    private final Provider<SecurityContext> securityContextProvider;
    private UserAuthModel userAuthModel;

    @Inject
    public SecurityContextWrapper(@Context final Provider<SecurityContext> securityContextProvider) {
        this.securityContextProvider = securityContextProvider;
    }

    public UserAuthModel getUserInfo() {
        return userAuthModel;
    }

    public boolean isUserInRole(final String role) {
        if (userAuthModel == null) {
            UserPrincipal userPrincipal = (UserPrincipal) this.securityContextProvider.get().getUserPrincipal();
            userAuthModel = userPrincipal.getUser();
            Preconditions.checkNotNull(userAuthModel);
        }
        return this.securityContextProvider.get().isUserInRole(role);
    }
}
