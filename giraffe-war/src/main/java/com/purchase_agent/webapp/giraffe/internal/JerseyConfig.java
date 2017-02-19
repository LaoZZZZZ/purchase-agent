package com.purchase_agent.webapp.giraffe.internal;


import com.googlecode.objectify.ObjectifyService;
import com.purchase_agent.webapp.giraffe.greetings.Greetings;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import com.purchase_agent.webapp.giraffe.objectify_entity.Entities;

public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        // Enable Spring DI
        register(RequestContextFilter.class);
        registerEntities();
	    register(JacksonObjectMapperConfig.class);
	
	    // Application resource
	    register(Greetings.class);

    }

    private void registerEntities() {
        for (final Object ob : Entities.entities) {
            ObjectifyService.register((java.lang.Class)ob);
        }
    }
}
