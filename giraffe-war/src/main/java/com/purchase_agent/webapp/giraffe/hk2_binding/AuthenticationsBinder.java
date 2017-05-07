package com.purchase_agent.webapp.giraffe.hk2_binding;

import com.purchase_agent.webapp.giraffe.authentication.InternalTrafficAuthentication;
import com.purchase_agent.webapp.giraffe.authentication.WhiteListedUserAuthentication;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * Created by lukez on 5/6/17.
 */
public class AuthenticationsBinder extends AbstractBinder {
    @Override
    public void configure() {
        bindAsContract(WhiteListedUserAuthentication.class);
        bindAsContract(InternalTrafficAuthentication.class);
    }
}
