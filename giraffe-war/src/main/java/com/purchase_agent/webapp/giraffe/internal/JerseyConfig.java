package com.purchase_agent.webapp.giraffe.internal;

import com.purchase_agent.webapp.giraffe.greetings.Greetings;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        // Enable Spring DI
        register(RequestContextFilter.class);
	register(JacksonObjectMapperConfig.class);
	
	// Application resource
	register(Greetings.class);
    }
}
