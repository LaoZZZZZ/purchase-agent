package com.purchase_agent.webapp.giraffe.hk2_binding;

import com.purchase_agent.webapp.giraffe.authentication.SecurityContextWrapper;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by lukez on 4/24/17.
 */
public class SecurityContextWrapperBinder extends AbstractBinder {
    private static class SecurityContextWrapperFactory  implements Factory<SecurityContextWrapper> {
        private final SecurityContextWrapper securityContextWrapper;

        @Inject
        SecurityContextWrapperFactory(@Context final SecurityContext securityContext) {
            this.securityContextWrapper = new SecurityContextWrapper(securityContext);
        }

        @Override
        public SecurityContextWrapper provide() {
            return this.securityContextWrapper;
        }

        @Override
        public void dispose(final SecurityContextWrapper securityContextWrapper) {
        }
    }

    public SecurityContextWrapperBinder() {
    }

    @Override
    public void configure() {
        bindFactory(SecurityContextWrapperFactory.class)
                .to(SecurityContextWrapper.class).in(RequestScoped.class);
    }
}
