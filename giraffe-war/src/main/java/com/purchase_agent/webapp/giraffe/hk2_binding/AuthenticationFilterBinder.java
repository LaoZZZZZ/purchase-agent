package com.purchase_agent.webapp.giraffe.hk2_binding;

import com.purchase_agent.webapp.giraffe.authentication.UserAuthModelHandler;
import com.purchase_agent.webapp.giraffe.filters.AuthenticationFilter;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Context;

/**
 * Created by lukez on 2/28/17.
 */
public class AuthenticationFilterBinder extends AbstractBinder {
    /*
    private static class AuthenticationFilterFactory implements Factory<AuthenticationFilter> {
        private AuthenticationFilter authenticationFilter;
        @Inject
        public AuthenticationFilterFactory(final UserAuthModelHandler userAuthModelHandler,
                                           @RequestTime final DateTime now) {
            this.authenticationFilter = new AuthenticationFilter(userAuthModelHandler, now);
        }

        @Override
        public AuthenticationFilter provide() {
            return this.authenticationFilter;
        }

        @Override
        public void dispose(final AuthenticationFilter filter) {
        }
    }
    */
    @Override
    public void configure() {
        bindAsContract(AuthenticationFilter.class);
        //bindFactory(AuthenticationFilterFactory.class).to(AuthenticationFilter.class).in(Singleton.class);
    }
}
