package com.purchase_agent.webapp.giraffe.authentication;

import com.google.common.collect.ImmutableSet;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Set;
/**
 * Created by lukez on 3/12/17.
 */
public class PASecurityContext implements SecurityContext {
    // TODO(lukez): add google auth shcema in the future.
    public static final Set<String> schemas = ImmutableSet.of("token", "white_listed_user");

    private String authSchema;
    private final Set<String> roles;
    private final Principal principal;

    private PASecurityContext(final Set<String> roles, final Principal principal, final String schema) {
        this.roles = roles;
        this.principal = principal;
        this.authSchema = schema;
    }

    public static PASecurityContext createSecurityContext(final Set<String> roles, final Principal principal,
                                                          final String schema) {
        return new PASecurityContext(roles, principal, schema);
    }

    @Override
    public boolean isUserInRole(final String role) {
        return roles.contains(role);
    }

    @Override
    public boolean isSecure() {
        return schemas.contains(this.authSchema);
    }

    @Override
    public Principal getUserPrincipal() {
        return this.principal;
    }

    @Override
    public String getAuthenticationScheme() {
        return this.authSchema;
    }

}
