package com.purchase_agent.webapp.giraffe.hk2_binding;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import com.purchase_agent.webapp.giraffe.filters.SensitiveInfoFilter;

import javax.inject.Singleton;

/**
 * Created by lukez on 2/27/17.
 */
public class SensitiveInfoFilterBinder extends AbstractBinder {
    private static class SensitiveInfoFilterFactory  implements Factory<SensitiveInfoFilter> {
        private SensitiveInfoFilter sensitiveInfoFilter = new SensitiveInfoFilter();
        @Override
        public SensitiveInfoFilter provide() {
            return this.sensitiveInfoFilter;
        }

        @Override
        public void dispose(final SensitiveInfoFilter filer) {

        }
    }

    @Override
    public void configure() {
        bindFactory(SensitiveInfoFilterFactory.class).to(SensitiveInfoFilter.class).in(Singleton.class);
    }
}
