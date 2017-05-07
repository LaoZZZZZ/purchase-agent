package com.purchase_agent.webapp.giraffe.hk2_binding;

import com.purchase_agent.webapp.giraffe.filters.UserLoginFilter;
import com.purchase_agent.webapp.giraffe.authentication.UserAuthModelHandler;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by lukez on 5/6/17.
 */
public class UserLoginFilterBinder extends AbstractBinder {
    private static class UserLoginFilterFactory implements Factory<UserLoginFilter> {
        private UserLoginFilter userLoginFilter;

        @Inject
        public UserLoginFilterFactory(final UserAuthModelHandler userAuthModelHandler,
                                      @RequestTime final javax.inject.Provider<DateTime> now) {
            this.userLoginFilter = new UserLoginFilter(userAuthModelHandler, now);
        }

        @Override
        public UserLoginFilter provide() {
            return this.userLoginFilter;
        }

        @Override
        public void dispose(final UserLoginFilter filter) {
        }
    }

    @Override
    public void configure() {
        bindFactory(UserLoginFilterFactory.class).to(UserLoginFilter.class).in(Singleton.class);
    }
}
