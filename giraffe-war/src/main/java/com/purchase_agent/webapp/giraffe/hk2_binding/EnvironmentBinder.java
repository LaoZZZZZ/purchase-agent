package com.purchase_agent.webapp.giraffe.hk2_binding;

import com.purchase_agent.webapp.giraffe.utils.Environment;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by lukez on 2/20/17.
 */
public class EnvironmentBinder extends AbstractBinder {
    private static class EnvironmentFactory implements Factory<Environment> {
        private Environment environment = Environment.PRODUCTION;

        @Inject
        public EnvironmentFactory() {
        }

        @Override
        public Environment provide() {
            return this.environment;
        }

        @Override
        public void dispose(final Environment environment) {}
    }
    @Override
    public void configure() {
        bindFactory(EnvironmentFactory.class).to(Environment.class).in(Singleton.class);
    }
}
