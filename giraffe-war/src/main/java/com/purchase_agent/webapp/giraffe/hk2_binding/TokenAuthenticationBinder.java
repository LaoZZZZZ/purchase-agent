package com.purchase_agent.webapp.giraffe.hk2_binding;

import com.purchase_agent.webapp.giraffe.authentication.TokenAuthentication;
import com.purchase_agent.webapp.giraffe.authentication.UserAuthModelHandler;
import com.purchase_agent.webapp.giraffe.internal.RequestTime;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by lukez on 5/6/17.
 */
public class TokenAuthenticationBinder extends AbstractBinder {
    private static class TokenAuthenticationFactory implements Factory<TokenAuthentication> {
        private TokenAuthentication tokenAuthentication;
        @Inject
        TokenAuthenticationFactory(final UserAuthModelHandler userAuthModelHandler,
                                   @RequestTime final javax.inject.Provider<DateTime> now) {
            this.tokenAuthentication = new TokenAuthentication(userAuthModelHandler, now);
        }
        @Override
        public TokenAuthentication provide() {
            return this.tokenAuthentication;
        }

        @Override
        public void dispose(final TokenAuthentication tokenAuthentication) {
        }
    }
    @Override
    public void configure() {
        bindFactory(TokenAuthenticationFactory.class).to(TokenAuthentication.class).in(Singleton.class);
    }
}
