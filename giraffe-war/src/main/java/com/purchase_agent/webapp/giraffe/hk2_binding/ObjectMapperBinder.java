package com.purchase_agent.webapp.giraffe.hk2_binding;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by lukez on 2/28/17.
 */
public class ObjectMapperBinder extends AbstractBinder {
    private static class ObjectMapperFactory implements Factory<ObjectMapper> {
        private ObjectMapper objectMapper;

        @Inject
        public ObjectMapperFactory() {
            this.objectMapper = createDefaultMapper();
        }
        @Override
        public ObjectMapper provide() {
            return this.objectMapper;
        }

        @Override
        public void dispose(final ObjectMapper objectMapper) {}
    }
    @Override
    public void configure() {
        bindFactory(ObjectMapperFactory.class).to(ObjectMapper.class).in(Singleton.class);
    }

    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper result = new ObjectMapper().registerModule(new JodaModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(MapperFeature.AUTO_DETECT_CREATORS)
                .disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS);
        return result;
    }
}
