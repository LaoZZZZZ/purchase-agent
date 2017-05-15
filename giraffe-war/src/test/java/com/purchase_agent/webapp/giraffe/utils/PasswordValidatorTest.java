package com.purchase_agent.webapp.giraffe.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for password validator.
 * Created by lukez on 3/17/17.
 */
public class PasswordValidatorTest {
    private PasswordValidator passwordValidator = new PasswordValidator();
    @Test
    public void test_validPassword_success() {
        final String validPassword = "test_pass_word";
        Assert.assertTrue(passwordValidator.isValidPassword(validPassword));
    }

    @Test
    public void test_invalidPassword_success() {
        final String invalidPasswor = "<>t";
        Assert.assertFalse(passwordValidator.isValidPassword(invalidPasswor));

    }
}
