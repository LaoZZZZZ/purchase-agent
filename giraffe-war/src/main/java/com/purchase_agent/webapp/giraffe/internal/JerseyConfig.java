package com.purchase_agent.webapp.giraffe.internal;

import com.purchase_agent.webapp.giraffe.greetings.Greetings;
import com.purchase_agent.webapp.giraffe.hk2_binding.EnvironmentBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.LinksBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.PasswordValidatorBinder;
import com.purchase_agent.webapp.giraffe.resource.UserResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        // Enable Spring DI
        register(RequestContextFilter.class);
        register(JacksonObjectMapperConfig.class);

        // Application resources
        register(Greetings.class);
        register(UserResource.class);

        // Application binders
        register(new PasswordValidatorBinder());
        register(new LinksBinder());
        register(new EnvironmentBinder());
    }
}
