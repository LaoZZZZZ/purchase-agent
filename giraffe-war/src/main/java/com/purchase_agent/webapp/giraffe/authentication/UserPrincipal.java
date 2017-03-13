package com.purchase_agent.webapp.giraffe.authentication;

import java.security.Principal;

/**
 *
 * Created by lukez on 3/12/17.
 */
public class UserPrincipal implements Principal {
    private UserAuthModel user;

    public UserPrincipal(final UserAuthModel userAuth) {
        this.user = userAuth;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    public UserAuthModel getUser() {
        return user;
    }
}
