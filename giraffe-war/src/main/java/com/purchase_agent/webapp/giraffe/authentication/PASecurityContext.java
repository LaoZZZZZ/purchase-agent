package com.purchase_agent.webapp.giraffe.authentication;

import com.google.common.collect.ImmutableSet;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Set;
/**
 * Created by lukez on 3/12/17.
 */
public class PASecurityContext implements SecurityContext {
    public enum Schema {
        TOKEN("token"),
        INTERNAL("internal"),
        WHITE_LISTED("white_listed");

        private String name;

        Schema(final String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
    // TODO(lukez): add google auth shcema in the future.
    public static final Set<Schema> schemas = ImmutableSet.of(Schema.TOKEN, Schema.WHITE_LISTED);

    private Schema authSchema;
    private final Set<String> roles;
    private final Principal principal;

    private PASecurityContext(final Set<String> roles, final Principal principal, final Schema schema) {
        this.roles = roles;
        this.principal = principal;
        this.authSchema = schema;
    }

    public static PASecurityContext createSecurityContext(final Set<String> roles, final Principal principal,
                                                          final Schema schema) {
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
        return this.authSchema.toString();
    }

}
