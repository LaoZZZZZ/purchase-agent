package com.purchase_agent.webapp.giraffe.internal;

import com.googlecode.objectify.ObjectifyService;
import com.purchase_agent.webapp.giraffe.hk2_binding.ObjectMapperBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.AuthenticationFilterBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.EnvironmentBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.LinksBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.PasswordValidatorBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.RequestTimeBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.SensitiveInfoFilterBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.UserAuthModelHandlerBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.UserAuthResponseFilterBinder;
import com.purchase_agent.webapp.giraffe.objectify_entity.Entities;
import com.purchase_agent.webapp.giraffe.resource.TransactionsResource;
import com.purchase_agent.webapp.giraffe.resource.UserResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;

public class JerseyConfig extends ResourceConfig {
    private static final Class[] RESOURCES = new Class[]{
            TransactionsResource.class,
            UserResource.class,
    };
    static {
        JodaTimeTranslators.add(ObjectifyService.factory());
        for (Class<?> clazz : Entities.entities) {
            ObjectifyService.register(clazz);
        }

    }
    public JerseyConfig() {
        super(RESOURCES);
        // Enable Spring DI
        register(RequestContextFilter.class);
        register(JacksonObjectMapperConfig.class);
        register(new ObjectMapperBinder());

        // Application binders
        register(new PasswordValidatorBinder());
        register(new LinksBinder());
        register(new EnvironmentBinder());
        register(new UserAuthModelHandlerBinder());
        register(new SensitiveInfoFilterBinder());
        register(new UserAuthResponseFilterBinder());
        register(new AuthenticationFilterBinder());
        register(new RequestTimeBinder());
    }

}
