package com.purchase_agent.webapp.giraffe.hk2_binding;

import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.joda.time.DateTime;

/**
 * Created by lukez on 3/9/17.
 */
public class RequestTimeBinder extends AbstractBinder {
    public static class RequestTimeFactory implements Factory<DateTime> {
        @Override
        public DateTime provide() {
            return DateTime.now();
        }

        @Override
        public void dispose(final DateTime requestTime) {
        }
    }

    @Override
    public void configure() {
        bindFactory(RequestTimeFactory.class).to(DateTime.class).qualifiedBy(new RequestTime.Impl()).in(RequestScoped.class);
    }
}
