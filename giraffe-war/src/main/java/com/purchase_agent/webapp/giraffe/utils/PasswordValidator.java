package com.purchase_agent.webapp.giraffe.utils;

import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.google.appengine.repackaged.com.google.common.collect.ImmutableSet;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by lukez on 2/19/17.
 */
@Singleton
public class PasswordValidator {
    private static final Logger logger = Logger.getLogger(PasswordValidator.class.getName());
    private static final Charset charset = StandardCharsets.US_ASCII;

    @Inject
    public PasswordValidator() {
    }

    public boolean isValidPassword(final String password) {
        if (Strings.isNullOrEmpty(password)) {
            logger.warning("The given password is empty or null");
            return false;
        }

        return charset.newEncoder().canEncode(password);
    }
}
