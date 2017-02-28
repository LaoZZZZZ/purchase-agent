package com.purchase_agent.webapp.giraffe.hk2_binding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purchase_agent.webapp.giraffe.authentication.UserAuthModelHandler;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

/**
 * Created by lukez on 2/27/17.
 */
public class UserAuthModelHandlerBinder extends AbstractBinder {
    public static class UserAuthModelHandlerFactory implements Factory<UserAuthModelHandler> {
        private UserAuthModelHandler userAuthModelHandler;

        public UserAuthModelHandlerFactory(final ObjectMapper objectMapper) {
            this.userAuthModelHandler = new UserAuthModelHandler(objectMapper);
        }

        @Override
        public UserAuthModelHandler provide() {
            return this.userAuthModelHandler;
        }

        @Override
        public void dispose(final UserAuthModelHandler handler) {

        }
    }

    @Override
    public void configure() {
        bindFactory(UserAuthModelHandlerFactory.class).to(UserAuthModelHandler.class).to(Singleton.class);
    }
}
