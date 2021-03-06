package com.purchase_agent.webapp.giraffe.internal;

import com.googlecode.objectify.ObjectifyService;
import com.purchase_agent.webapp.giraffe.aggregator.MetricsAggregator.TransactionsAggregator;
import com.purchase_agent.webapp.giraffe.hk2_binding.AuthenticationsBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.ObjectMapperBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.AuthenticationFilterBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.EnvironmentBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.LinksBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.PasswordValidatorBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.RequestTimeBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.SensitiveInfoFilterBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.TokenAuthenticationBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.UserAuthModelHandlerBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.UserAuthResponseFilterBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.SecurityContextWrapperBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.AggregatorBinder;
import com.purchase_agent.webapp.giraffe.hk2_binding.UserLoginFilterBinder;
import com.purchase_agent.webapp.giraffe.objectify_entity.Entities;
import com.purchase_agent.webapp.giraffe.persistence.LineItemDao;
import com.purchase_agent.webapp.giraffe.persistence.TransactionDao;
import com.purchase_agent.webapp.giraffe.persistence.UserDao;
import com.purchase_agent.webapp.giraffe.resource.CronJobsResource;
import com.purchase_agent.webapp.giraffe.resource.CustomersResource;
import com.purchase_agent.webapp.giraffe.resource.ExpressCompaniesResource;
import com.purchase_agent.webapp.giraffe.resource.LineItemsResource;
import com.purchase_agent.webapp.giraffe.resource.TransactionsResource;
import com.purchase_agent.webapp.giraffe.resource.UserResource;
import com.purchase_agent.webapp.giraffe.resource.AdministratorResource;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;

public class JerseyConfig extends ResourceConfig {
    private static final Class[] RESOURCES = new Class[]{
            AdministratorResource.class,
            CronJobsResource.class,
            CustomersResource.class,
            ExpressCompaniesResource.class,
            LineItemsResource.class,
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

        register(new Binder());

        // Application binders
        register(new RequestTimeBinder());
        register(new PasswordValidatorBinder());
        register(new LinksBinder());
        register(new EnvironmentBinder());
        register(new UserAuthModelHandlerBinder());
        register(new SensitiveInfoFilterBinder());
        register(new UserAuthResponseFilterBinder());
        register(new AuthenticationFilterBinder());
        register(new SecurityContextWrapperBinder());
        register(new TokenAuthenticationBinder());
        register(new UserLoginFilterBinder());
        register(new AuthenticationsBinder());
        register(new AggregatorBinder());
    }

    public static class Binder extends AbstractBinder {
        @Override
        protected void configure() {
            // DAO class
            bindAsContract(TransactionDao.class);
            bindAsContract(UserDao.class);
            bindAsContract(LineItemDao.class);

            bindAsContract(TransactionsAggregator.class);
        }
    }
}
