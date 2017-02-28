package com.purchase_agent.webapp.giraffe.hk2_binding;

import com.purchase_agent.webapp.giraffe.filters.UserAuthResponseFilter;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by lukez on 2/28/17.
 */
public class UserAuthResponseFilterBinder extends AbstractBinder {
    private static class UserAuthResponseFilterFactory implements Factory<UserAuthResponseFilter> {
        private UserAuthResponseFilter userAuthResponseFilter;
        @Inject
        public UserAuthResponseFilterFactory() {
            this.userAuthResponseFilter = new UserAuthResponseFilter();
        }

        @Override
        public UserAuthResponseFilter provide() {
            return this.userAuthResponseFilter;
        }

        @Override
        public void dispose(final UserAuthResponseFilter filter) {
        }
    }

    @Override
    public void configure() {
        bindFactory(UserAuthResponseFilterFactory.class).to(UserAuthResponseFilter.class).in(Singleton.class);
    }
}
