package com.purchase_agent.webapp.giraffe.hk2_binding;

import com.purchase_agent.webapp.giraffe.utils.PasswordValidator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.hk2.api.Factory;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by lukez on 2/20/17.
 */
public class PasswordValidatorBinder extends AbstractBinder{
    private static class PasswordValidatorFactory  implements Factory<PasswordValidator> {
        private final PasswordValidator passwordValidator;

        @Inject
        PasswordValidatorFactory() {
            this.passwordValidator = new PasswordValidator();
        }

        @Override
        public PasswordValidator provide() {
            return this.passwordValidator;
        }

        @Override
        public void dispose(final PasswordValidator passwordValidator) {
        }
    }

    public PasswordValidatorBinder() {
    }

    @Override
    public void configure() {
        bindFactory(PasswordValidatorFactory.class).to(PasswordValidator.class).in(Singleton.class);
    }
}
