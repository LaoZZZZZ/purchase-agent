package com.purchase_agent.webapp.giraffe.hk2_binding;

import com.purchase_agent.webapp.giraffe.utils.Links;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.hk2.api.Factory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.UriInfo;

/**
 * Created by lukez on 2/20/17.
 */
public class LinksBinder extends AbstractBinder {
    private static class LinksBinderFactory implements Factory<Links> {
        private Links links;

        @Inject
        public LinksBinderFactory(final UriInfo uriInfo) {
            this.links = new Links(uriInfo);
        }
        @Override
        public Links provide() {
            return this.links;
        }

        @Override
        public void dispose(final Links links) {
        }
    }

    public LinksBinder() {
    }

    @Override
    public void configure() {
        bindFactory(LinksBinderFactory.class).to(Links.class).in(Singleton.class);
    }
}
