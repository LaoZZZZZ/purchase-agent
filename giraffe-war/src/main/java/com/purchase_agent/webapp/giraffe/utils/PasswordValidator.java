package com.purchase_agent.webapp.giraffe.utils;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.Set;
/**
 * Created by lukez on 2/19/17.
 */
@Singleton
public class PasswordValidator {
    private static final Logger logger = Logger.getLogger(PasswordValidator.class.getName());

    public static class PasswordCharset {
        private static final char[] dict = {
                '@', '#', '$', '&','*', '?', '-', '_', '+', '=', '^', '.', '/', ',', '!','%','(', ')','|','`', '~',
                'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                '0','1','2','3','4','5','6','7','8','9',' '};
        private static final CharMatcher mather = CharMatcher.anyOf(Arrays.toString(dict)).negate();

        public PasswordCharset() {
        }

        public boolean contains(final String password) {
            return !mather.matchesAnyOf(password);
        }
    }
    //private static final Charset charset = StandardCharsets.US_ASCII;
    private PasswordCharset charset = new PasswordCharset();

    @Inject
    public PasswordValidator() {
    }

    public boolean isValidPassword(final String password) {
        if (Strings.isNullOrEmpty(password)) {
            logger.warning("The given password is empty or null");
            return false;
        }
        return charset.contains(password);
    }
}
